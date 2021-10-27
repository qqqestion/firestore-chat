package ru.tashkent.messenger.ui.mychats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.tashkent.domain.models.Chat
import ru.tashkent.messenger.databinding.ItemChatBinding
import ru.tashkent.messenger.models.chatDiffUtilCallback
import ru.tashkent.messenger.vhs.ChatViewHolder

class ChatAdapter(
    val onChatClick: (Chat) -> Unit
) : ListAdapter<Chat, ChatViewHolder>(chatDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onChatClick
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}