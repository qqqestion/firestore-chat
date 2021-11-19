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
    val senderId: String? = null,
    val text: String? = null,
    @ServerTimestamp val timeSent: Timestamp? = null,
    @get:Exclude val fromCurrentUser: Boolean = false,
    @get:Exclude val senderName: String = ""
) {

    fun toMessage() = Message(
        id!!,
        chatId!!,
        senderId!!,
        senderName,
        text!!,
        timeSent!!.millis,
        fromCurrentUser
    )

    companion object {

        val FIELD_CHAT_ID = "chatId"
        val FIELD_SENDER_ID = "senderId"
        val FIELD_TEXT = "text"
        val FIELD_TIME_SENT = "timeSent"
    }
}

fun Message.toFirebaseMessage() = FirebaseMessage(
    id,
    chatId,
    senderId,
    text,
    Timestamp(Date(timeSent)),
    fromCurrentUser,
    senderName
)
