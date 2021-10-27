package ru.tashkent.messenger.vhs

import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.tashkent.domain.models.Message
import ru.tashkent.messenger.R
import ru.tashkent.messenger.databinding.ItemMessageBinding

class MessageViewHolder(
    private val binding: ItemMessageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: Message) {
        if (message.fromCurrentUser) {
            binding.root.gravity = Gravity.END
            binding.tvMessageText.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.midnight_green
                )
            )
        } else {
            binding.root.gravity = Gravity.START
            binding.tvMessageText.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.light_green
                )
            )
        }
        binding.tvMessageText.text = message.text
    }
}