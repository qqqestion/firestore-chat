package ru.tashkent.messenger.ui.setinfo

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
import ru.tashkent.messenger.databinding.FragmentSetInfoBinding
import ru.tashkent.messenger.exts.appComponent
import ru.tashkent.messenger.exts.clearErrorOnAnyInput
import ru.tashkent.messenger.exts.showErrorResId
import ru.tashkent.messenger.exts.textOrEmpty
import ru.tashkent.messenger.viewbinding.viewBinding
import javax.inject.Inject

class SetInfoFragment : Fragment(R.layout.fragment_set_info) {

    private val binding: FragmentSetInfoBinding by viewBinding()

    @Inject
    lateinit var factory: Lazy<SetInfoViewModel.Factory>

    private val viewModel: SetInfoViewModel by viewModels { factory.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSubmit.setOnClickListener {
            viewModel.submit(binding.tilName.textOrEmpty())
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::handleState)
            }
        }
        binding.tilName.clearErrorOnAnyInput()
    }

    private fun handleState(state: SetInfoViewModel.State) {
        val isNotLoading = state != SetInfoViewModel.State.Loading
        binding.tvSubmit.isEnabled = isNotLoading
        binding.progressBar.isInvisible = isNotLoading
        when (state) {
            SetInfoViewModel.State.InputError -> binding.tilName.showErrorResId(R.string.error_invalid_name)
            SetInfoViewModel.State.Loading, SetInfoViewModel.State.Empty -> Unit
            SetInfoViewModel.State.Success -> findNavController()
                .navigate(SetInfoFragmentDirections.actionSetInfoFragmentToMyChatsFragment())
        }
    }
}