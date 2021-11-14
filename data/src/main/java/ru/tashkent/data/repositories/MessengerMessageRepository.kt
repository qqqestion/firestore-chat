package ru.tashkent.data.repositories

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.tasks.await
import logcat.LogPriority
import logcat.asLog
import logcat.logcat
import ru.tashkent.data.exts.awaitResult
import ru.tashkent.data.exts.awaitTask
import ru.tashkent.data.models.FirebaseMessage
import ru.tashkent.domain.Either
import ru.tashkent.domain.mapRight
import ru.tashkent.domain.repositories.MessageRepository
import ru.tashkent.domain.models.Message
import java.util.*
import javax.inject.Inject

internal class MessengerMessageRepository @Inject constructor() : MessageRepository {

    companion object {

        private const val MESSAGES_COLLECTION = "messages"
    }

    private val messagesCollection = FirebaseFirestore.getInstance().collection(MESSAGES_COLLECTION)

    private val messagesData = MutableSharedFlow<Message>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val messages: Flow<Message>
        get() = messagesData.asSharedFlow()

    override fun initMessages(chatId: String, lastMessageTimeSent: Long) {
        messagesCollection
            .whereEqualTo("chatId", chatId)
            .orderBy("timeSent")
            .startAfter(Timestamp(Date(lastMessageTimeSent)))
            .addSnapshotListener { value, error ->
                if (error != null) {
                    logcat(LogPriority.ERROR) { error.asLog() }
                    return@addSnapshotListener
                }
                value?.documentChanges?.forEach {
                    // pending writes = false && type = modified - add timestamp to current user message
                    // pending writes = true && type = added - add current user message
                    // pending writes = false && type = added - add another user message
                    // pending writes = true && type = modified - change current user message

                    fun isNewMessageFromCurrentUser(doc: DocumentChange) =
                        doc.type == DocumentChange.Type.MODIFIED &&
                                doc.document.metadata.hasPendingWrites().not()

                    fun isNewMessageFromAnotherUsers(doc: DocumentChange) =
                        doc.type == DocumentChange.Type.ADDED &&
                                doc.document.metadata.hasPendingWrites().not()

                    if (isNewMessageFromCurrentUser(it) or isNewMessageFromAnotherUsers(it)) {
                        val message = it.document.toObject(FirebaseMessage::class.java)
                        logcat { "New message: $message" }
                        messagesData.tryEmit(
                            message.copy(fromCurrentUser = message.senderId == FirebaseAuth.getInstance().uid)
                                .toMessage()
                        )
                    }
                }
            }
    }

    override suspend fun getMessagesByChatId(chatId: String): Either<Throwable, List<Message>> =
        messagesCollection
            .whereEqualTo("chatId", chatId)
            .orderBy("timeSent")
            .get()
            .awaitTask()
            .mapRight { doc ->
                doc.mapNotNull {
                    it.toObject(FirebaseMessage::class.java)
                        .copy(fromCurrentUser = it["sender"] as String == FirebaseAuth.getInstance().uid)
                        .toMessage()
                }
            }

    override suspend fun sendMessage(chatId: String, messageText: String) {
        val message = FirebaseMessage(
            "",
            chatId,
            FirebaseAuth.getInstance().uid!!,
            messageText,
            null
        )
        messagesCollection
            .add(message)
            .await()
    }

    override suspend fun deleteMessagesInChat(chatId: String) {
        messagesCollection
            .whereEqualTo("chatId", chatId)
            .get()
            .await()
            .documents
            .onEach {
                it.reference.delete().await()
            }
    }
}
