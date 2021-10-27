package ru.tashkent.messenger.ui.mychats

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.tashkent.data.models.toFirebaseChat
import ru.tashkent.messenger.R
import ru.tashkent.messenger.databinding.FragmentMychatsBinding
import ru.tashkent.messenger.viewbinding.viewBinding

class MyChatsFragment : Fragment(R.layout.fragment_mychats) {

    private val binding by viewBinding<FragmentMychatsBinding>()

    private val viewModel by viewModels<MyChatsViewModel>()

    private val chatsAdapter = ChatAdapter { chat ->
        findNavController().navigate(
            MyChatsFragmentDirections.actionMyChatsFragmentToChatFragment(
                chat.toFirebaseChat()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("!!!", "onViewCreated")

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::handleState)
            }
        }
        setupRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshChats()
        }
    }

    private fun setupRecyclerView() = with(binding.rvChats) {
        adapter = chatsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleState(state: MyChatsState) {
        when (state) {
            MyChatsState.Loading -> {
                binding.swipeRefreshLayout.isRefreshing = true
            }
            is MyChatsState.Failure -> {
                binding.swipeRefreshLayout.isRefreshing = false
                Snackbar.make(requireView(), R.string.error_unknown, Snackbar.LENGTH_SHORT).show()
            }
            is MyChatsState.Success -> {
                binding.swipeRefreshLayout.isRefreshing = false
                chatsAdapter.submitList(state.chats)
            }
        }
    }

}