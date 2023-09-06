package com.manila.fasaldoctor.model

import android.os.Parcel
import android.os.Parcelable

data class User(
//    var userId : String? = null,
    var name: String? = null,
    var email: String? = null,
    var role: String? = null,
    val uid: String? = null,
    val fcmtoken: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(role)
        parcel.writeString(uid)
        parcel.writeString(fcmtoken)
        parcel.writeString(description)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}


//    companion object {
////        val imageURl: Any? = null
//        var imageUrl: Map<String, String>? = null
//    }
//


