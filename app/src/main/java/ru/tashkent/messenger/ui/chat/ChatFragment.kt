package ru.tashkent.messenger.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.tashkent.domain.models.Message
import ru.tashkent.messenger.R
import ru.tashkent.messenger.databinding.FragmentChatBinding
import ru.tashkent.messenger.exts.appComponent
import ru.tashkent.messenger.viewbinding.viewBinding
import javax.inject.Inject

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val binding by viewBinding<FragmentChatBinding>()
    private val args by navArgs<ChatFragmentArgs>()

    @Inject
    lateinit var factory: ChatViewModel.ViewModelFactory.Factory

    private val viewModel by viewModels<ChatViewModel> {
        factory.create(args.chat.id!!)
    }

    private val messagesAdapter = MessageAdapter()

    override fun onAttach(context: Context) {
        requireContext().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newMessages.collect(::handleNewMessages)
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messages.collect(::handleMessages)
            }
        }

        binding.etInputMessage.addTextChangedListener {
            binding.ivSend.isEnabled = it?.toString()?.isNotEmpty() ?: false
        }

        binding.ivSend.isEnabled = false
        binding.ivSend.setOnClickListener {
            viewModel.sendMessage(args.chat.id!!, binding.etInputMessage.text.toString())
            binding.etInputMessage.setText("")
        }

        binding.ivOptions.setOnClickListener {
            viewModel.deleteMessages(args.chat.id!!)
        }
    }

    private fun setupRecyclerView() = with(binding.rvMessages) {
        adapter = messagesAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleNewMessages(message: Message) {
        messagesAdapter.submitList(messagesAdapter.currentList + message)
    }

    private fun handleMessages(messages: List<Message>) {
        messagesAdapter.submitList(messages)
    }
}