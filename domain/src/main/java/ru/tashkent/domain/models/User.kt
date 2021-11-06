package ru.tashkent.domain.models

data class User(
    val name: Name
) {

    @JvmInline
    value class Email private constructor(val value: String) {

        companion object {

            fun createIfValid(email: String): Email? {
                if (email.isEmpty()) return null
                return Email(email)
            }
        }
    }

    @JvmInline
    value class Password private constructor(val value: String) {

        companion object {

            fun createIfValid(password: String): Password? {
                if (password.length < 6) return null
                return Password(password)
            }
        }
    }

    @JvmInline
    value class Name private constructor(val value: String) {

        companion object {

            fun createIfValid(name: String): Name? {
                if (name.isEmpty()) return null
                return Name(name)
            }
        }
    }
}