package ru.tashkent.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.tashkent.data.exts.awaitTask
import ru.tashkent.domain.EmptyEither
import ru.tashkent.domain.mapLeft
import ru.tashkent.domain.mapRight
import ru.tashkent.domain.models.User
import ru.tashkent.domain.repositories.AuthRepository
import javax.inject.Inject

internal class MessengerAuthRepository @Inject constructor() : AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    override suspend fun isAuthorized(): Boolean = auth.uid != null && checkIfAdditionalInfoSet()

    override suspend fun login(
        email: User.Email,
        password: User.Password
    ): EmptyEither<AuthRepository.LoginError> = auth
        .signInWithEmailAndPassword(email.value, password.value)
        .awaitTask()
        .mapRight { }
        .mapLeft {
            when (it) {
                is FirebaseAuthInvalidUserException -> AuthRepository.LoginError.Unknown // can't happen I think
                is FirebaseAuthInvalidCredentialsException -> AuthRepository.LoginError.InvalidPassword
                else -> AuthRepository.LoginError.Unknown
            }
        }

    override suspend fun checkIfAdditionalInfoSet(): Boolean = try {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().uid!!)
            .get()
            .await()
            .exists()
    } catch (e: Exception) {
        Log.d("", "", e)
        false
    }

    override suspend fun createAccount(
        email: User.Email,
        password: User.Password
    ): EmptyEither<AuthRepository.RegistrationError> = auth
        .createUserWithEmailAndPassword(email.value, password.value)
        .awaitTask()
        .mapRight {}
        .mapLeft {
            when (it) {
                is FirebaseAuthUserCollisionException -> AuthRepository.RegistrationError.UserExists
                else -> AuthRepository.RegistrationError.Unknown
            }
        }
}
