package ru.tashkent.messenger.vhs

import androidx.recyclerview.widget.RecyclerView
import ru.tashkent.domain.models.Chat
import ru.tashkent.messenger.databinding.ItemChatBinding

class ChatViewHolder(
    private val binding: ItemChatBinding,
    private val onChatClick: ((Chat) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: Chat) {
        binding.tvChatName.text = chat.name
        binding.root.setOnClickListener {
            onChatClick?.invoke(chat)
        }
    }
}
