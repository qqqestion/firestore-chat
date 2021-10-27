/**
 * Was created by Sarthak Garg
 * https://medium.com/flobiz-blog/fragment-view-binding-initialisation-using-delegates-8cd50b41e1d2
 */
package ru.tashkent.messenger.viewbinding

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    viewBindingBind: ((View) -> T)? = null,
    viewBindingClazz: Class<T>? = null
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null
    private val bind: (View) -> T

    init {
        ensureMainThread()
        require(viewBindingBind != null || viewBindingClazz != null) {
            "Both viewBindingBind and viewBindingClazz are null. Please provide at least one."
        }
        bind = viewBindingBind ?: run {
            val method by lazy(LazyThreadSafetyMode.NONE) {
                viewBindingClazz!!.getMethod(
                    "bind",
                    View::class.java
                )
            }

            @Suppress("UNCHECKED_CAST")
            fun(view: View): T = method.invoke(null, view) as T
        }
        fragment.lifecycle.addObserver(FragmentLifecycleObserver())
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        ensureMainThread()
        binding?.let { return it }
        check(fragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            "Attempt to get view binding when fragment view is destroyed"
        }
        return bind(thisRef.requireView()).also { binding = it }
    }

    private inner class FragmentLifecycleObserver : DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) {
            fragment.viewLifecycleOwnerLiveData.observe(fragment) { lifecycleOwner: LifecycleOwner? ->
                lifecycleOwner ?: return@observe

                val viewLifecycleObserver = object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        lifecycleOwner.lifecycle.removeObserver(this)
                        Handler(Looper.getMainLooper()).post { binding = null }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(viewLifecycleObserver)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            fragment.lifecycle.removeObserver(this)
            binding = null
        }
    }

    private fun ensureMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw IllegalStateException("${FragmentViewBindingDelegate::class.simpleName} must be initialized in main thread")
        }
    }
}