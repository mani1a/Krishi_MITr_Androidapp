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
    var mobile : String? = null,
    var imageUrl: String? = null,
    var recent : String? = null,
    var crop1 : String?= null,
    var crop2 : String?= null,
    var crop3 : String?= null,

)



