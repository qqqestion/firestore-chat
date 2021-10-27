package ru.tashkent.notes.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.tashkent.domain.models.Message
import ru.tashkent.notes.databinding.ItemMessageBinding
import ru.tashkent.notes.models.messageDiffUtilCallback
import ru.tashkent.notes.vhs.MessageViewHolder

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