package ru.tashkent.messenger.models

import androidx.recyclerview.widget.DiffUtil
import ru.tashkent.domain.models.Chat

val chatDiffUtilCallback = object : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat) = oldItem == newItem
}