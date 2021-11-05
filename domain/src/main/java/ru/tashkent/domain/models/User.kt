package ru.tashkent.domain.models

data class User(
    val email: Email,
    val password: Password,
    val name: Name,
) {

    @JvmInline
    value class Email private constructor(val value: String) {

        companion object {

            fun createIfValid(email: String): Email? {
                return Email(email)
            }
        }
    }

    @JvmInline
    value class Password private constructor(val value: String) {

        companion object {

            fun createIfValid(password: String): Password? {
                return Password(password)
            }
        }
    }

    @JvmInline
    value class Name private constructor(val value: String) {

        companion object {

            fun createIfValid(name: String): Name? {
                return Name(name)
            }
        }
    }
}