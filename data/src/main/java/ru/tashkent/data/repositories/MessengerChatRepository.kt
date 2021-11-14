package ru.tashkent.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import ru.tashkent.data.exts.awaitResult
import ru.tashkent.data.exts.awaitTask
import ru.tashkent.data.models.FirebaseChat
import ru.tashkent.domain.Either
import ru.tashkent.domain.mapRight
import ru.tashkent.domain.repositories.ChatRepository
import ru.tashkent.domain.models.Chat
import javax.inject.Inject

internal class MessengerChatRepository @Inject constructor() : ChatRepository {

    companion object {

        private const val CHATS_COLLECTION = "chats"
    }

    private val chatsCollection = FirebaseFirestore.getInstance().collection(CHATS_COLLECTION)

    override suspend fun getChats(): Either<Throwable, List<Chat>> =
        chatsCollection.get().awaitTask()
            .mapRight { doc ->
                doc.documents.mapNotNull {
                    it.toObject(FirebaseChat::class.java)?.toChat()
                }
            }
}
