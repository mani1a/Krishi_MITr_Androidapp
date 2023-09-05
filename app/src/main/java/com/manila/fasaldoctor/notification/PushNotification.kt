package com.manila.fasaldoctor.notification

data class PushNotification(
    val data: NotificationData,
    val to : String? = "",
)
