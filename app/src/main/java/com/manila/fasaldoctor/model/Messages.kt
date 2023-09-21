package com.manila.fasaldoctor.model

import android.net.Uri

data class Messages(
    var recent : String? = null,
    var time: String? =null,
    var msg: String? = null,
    var senderID: String? = null,
    var receiverID : String? = null,
    var image: String? = null,
    var audio : String? = null

)
