package ru.tashkent.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import ru.tashkent.domain.models.Chat

data class FirebaseChat(
    @DocumentId val id: String? = null,
    val name: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(name)
    }

    fun toChat() = Chat(id!!, name!!)

    companion object CREATOR : Parcelable.Creator<FirebaseChat> {

        override fun createFromParcel(parcel: Parcel): FirebaseChat {
            return FirebaseChat(parcel)
        }

        override fun newArray(size: Int): Array<FirebaseChat?> {
            return arrayOfNulls(size)
        }
    }
}

fun Chat.toFirebaseChat() = FirebaseChat(id, name)
