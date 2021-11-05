package ru.tashkent.messenger.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.tashkent.messenger.R
import ru.tashkent.messenger.databinding.FragmentLoginBinding
import ru.tashkent.messenger.exts.appComponent
import ru.tashkent.messenger.exts.textOrEmpty
import ru.tashkent.messenger.viewbinding.viewBinding
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding<FragmentLoginBinding>()

    @Inject
    lateinit var factory: Lazy<LoginViewModel.Factory>
    private val viewModel by viewModels<LoginViewModel> { factory.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            viewModel.login(
                binding.tilEmail.textOrEmpty(),
                binding.tilPassword.textOrEmpty()
            )
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::handleState)
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effects.collect(::handleEffect)
            }
        }
    }

    private fun handleEffect(effect: LoginViewModel.Effect) {
        when (effect) {
            LoginViewModel.Effect.NavigateToSetupAccount -> findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSetInfoFragment())
            LoginViewModel.Effect.NavigateToMain -> findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToMyChatsFragment())
        }
    }

    private fun handleState(state: AuthState) {
        val isNotLoading = (state is AuthState.Loading).not()
        binding.tvLogin.isEnabled = isNotLoading
        binding.progressBar.isInvisible = isNotLoading
        when (state) {
            AuthState.Empty, AuthState.Done, AuthState.Loading -> Unit
            is AuthState.Error -> TODO()
            AuthState.InputError.EmailError -> TODO()
            AuthState.InputError.PasswordError -> TODO()
        }
    }
}
