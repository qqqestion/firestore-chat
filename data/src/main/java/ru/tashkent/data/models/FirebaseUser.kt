package ru.tashkent.data.models

import ru.tashkent.domain.models.User

data class FirebaseUser(
    val name: String
) {

    fun toUser(): User = User(User.Name.createIfValid(name)!!)
}

fun User.toFirebaseUser(): FirebaseUser = FirebaseUser(name.value)