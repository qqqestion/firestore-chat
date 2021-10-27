package ru.tashkent.messenger.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.tashkent.domain.models.Message
import ru.tashkent.messenger.databinding.ItemMessageBinding
import ru.tashkent.messenger.models.messageDiffUtilCallback
import ru.tashkent.messenger.vhs.MessageViewHolder

class MessageAdapter : ListAdapter<Message, MessageViewHolder>(messageDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}