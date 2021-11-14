package ru.tashkent.messenger.ui.splash

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.tashkent.messenger.R
import ru.tashkent.messenger.databinding.FragmentSetInfoBinding
import ru.tashkent.messenger.exts.appComponent
import ru.tashkent.messenger.viewbinding.viewBinding
import javax.inject.Inject

class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject
    lateinit var factory: SplashViewModel.Factory
    private val viewModel by viewModels<SplashViewModel> { factory }

    override fun onAttach(context: Context) {
        requireContext().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authorizedStatus.collect { isAuthorized ->
                    if (isAuthorized) {
                        findNavController()
                            .navigate(SplashFragmentDirections.actionSplashFragmentToMyChatsFragment())
                    } else {
                        findNavController()
                            .navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                    }
                }
            }
        }
    }
}