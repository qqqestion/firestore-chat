package ru.tashkent.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import ru.tashkent.data.awaitResult
import ru.tashkent.data.models.FirebaseChat
import ru.tashkent.domain.repositories.ChatRepository
import ru.tashkent.domain.models.Chat

internal class MessengerChatRepository internal constructor() : ChatRepository {

    companion object {

        private const val CHATS_COLLECTION = "chats"
    }

    private val chatsCollection = FirebaseFirestore.getInstance().collection(CHATS_COLLECTION)

    override suspend fun getChats(): Result<List<Chat>> =
        chatsCollection.get().awaitResult()
            .map { doc ->
                doc.documents.mapNotNull {
                    it.toObject(FirebaseChat::class.java)?.toChat()
                }
            }
}

fun ChatRepository(): ChatRepository = MessengerChatRepository()