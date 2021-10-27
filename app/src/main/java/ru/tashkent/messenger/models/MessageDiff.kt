package ru.tashkent.messenger.models

import androidx.recyclerview.widget.DiffUtil
import ru.tashkent.domain.models.Message

val messageDiffUtilCallback = object : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
}