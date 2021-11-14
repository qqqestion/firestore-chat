package ru.tashkent.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.tashkent.data.models.FirebaseUser
import ru.tashkent.data.models.toFirebaseUser
import ru.tashkent.domain.Either
import ru.tashkent.domain.EmptyEither
import ru.tashkent.domain.models.User
import ru.tashkent.domain.repositories.UserRepository
import ru.tashkent.domain.runEither
import javax.inject.Inject


class MessengerUserRepository @Inject constructor() : UserRepository {

    companion object {

        private const val USER_COLLECTION = "users"
    }

    private val usersCollection = FirebaseFirestore.getInstance().collection(USER_COLLECTION)

    private var savedUser: User? = null

    override suspend fun saveUser(user: User): EmptyEither<Throwable> = runEither {
        usersCollection.document(FirebaseAuth.getInstance().uid!!).set(user.toFirebaseUser())
            .await()
        savedUser = user
    }

    override suspend fun getUser(): Either<Throwable, User> = runEither {
        if (savedUser == null) {
            savedUser = usersCollection
                .document(FirebaseAuth.getInstance().uid!!)
                .get()
                .await()
                .toObject(FirebaseUser::class.java)
                ?.toUser()
        }
        return@runEither savedUser!!
    }

    override suspend fun getUserById(userId: String): Either<Throwable, User?> = runEither {
        usersCollection
            .document(userId)
            .get()
            .await()
            .toObject(FirebaseUser::class.java)
            ?.toUser()
    }
}