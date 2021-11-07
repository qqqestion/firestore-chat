package ru.tashkent.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import ru.tashkent.data.exts.millis
import ru.tashkent.domain.models.Message
import java.util.*

data class FirebaseMessage(
    @DocumentId val id: String? = null,
    val chatId: String? = null,
    val sender: String? = null,
    val text: String? = null,
    @ServerTimestamp val timeSent: Timestamp? = null,
    @get:Exclude val fromCurrentUser: Boolean = false
) {

    fun toMessage() = Message(
        id!!,
        chatId!!,
        sender!!,
        text!!,
        timeSent!!.millis,
        fromCurrentUser
    )
}

fun Message.toFirebaseMessage() = FirebaseMessage(
    id,
    chatId,
    sender,
    text,
    Timestamp(Date(timeSent)),
    fromCurrentUser
)
