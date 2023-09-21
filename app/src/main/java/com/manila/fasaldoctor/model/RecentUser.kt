package com.manila.fasaldoctor.model

data class RecentUser(
    var name: String? = null,
    var email: String? = null,
    var role: String? = null,
    val uid: String? = null,
//    val fcmtoken: String? = null,
//    var description: String? = null,
    var mobile : String? = null,
    var imageUrl: String? = null,
    var recent : String? = null,
)
