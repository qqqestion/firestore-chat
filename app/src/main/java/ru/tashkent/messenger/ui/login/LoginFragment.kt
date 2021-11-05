package ru.tashkent.messenger.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
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
                binding.tilEmail.editText?.text?.toString()?.trim().orEmpty(),
                binding.tilPassword.editText?.text?.toString()?.trim().orEmpty()
            )
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::handleState)
            }
        }
    }

    private fun handleState(state: AuthState) {
        val isLoading = state is AuthState.Loading
        binding.tvLogin.isEnabled = isLoading.not()
        when (state) {
            AuthState.Empty -> Unit
            AuthState.Loading -> TODO()
            is AuthState.Error -> TODO()
            AuthState.InputError.EmailError -> TODO()
            AuthState.InputError.PasswordError -> TODO()
            AuthState.LoginSuccess -> findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToMyChatsFragment())
            AuthState.NavigateToSetupAccount -> findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSetInfoFragment())
        }
    }
}
