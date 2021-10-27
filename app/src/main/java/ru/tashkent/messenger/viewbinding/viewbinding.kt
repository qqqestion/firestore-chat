/**
 * Was created by Sarthak Garg
 * https://medium.com/flobiz-blog/fragment-view-binding-initialisation-using-delegates-8cd50b41e1d2
 */
package ru.tashkent.messenger.viewbinding

import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


@MainThread
fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): FragmentViewBindingDelegate<T> =
    FragmentViewBindingDelegate(fragment = this, viewBindingBind = bind)

@MainThread
inline fun <reified T : ViewBinding> Fragment.viewBinding(): FragmentViewBindingDelegate<T> =
    FragmentViewBindingDelegate(fragment = this, viewBindingClazz = T::class.java)