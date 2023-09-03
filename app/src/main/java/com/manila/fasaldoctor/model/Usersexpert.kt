package com.manila.fasaldoctor.model

import android.os.Parcel
import android.os.Parcelable

data class Usersexpert(
    var name : String? = null,
    var email : String? = null,
    var role : String? = null,
    var description : String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Usersexpert> {
        override fun createFromParcel(parcel: Parcel): Usersexpert {
            return Usersexpert(parcel)
        }

        override fun newArray(size: Int): Array<Usersexpert?> {
            return arrayOfNulls(size)
        }
    }
}
