package com.manila.fasaldoctor.notification.api

import android.telecom.Call
import com.manila.fasaldoctor.notification.PushNotification
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers("Content-Type: " +
            "application/json",
        "Authorization: " +
                "key = AAAAbGzpBG0:APA91bG5XocUFZJtBf3TawacvoWSSt96SlaFDeWMDNBcdqEi_h4AntsSE0DBlmnYvpwWALQEx2dYGlTaVMSo7GS0cSVH4ytoqaZ-kRdwB9RU_R787gwU0j8BK5EGbE5FQt3BwDKdLbIN")

    @POST("fcm/send")

    fun sendNotification(@Body notification: PushNotification)
    : retrofit2.Call<PushNotification>





}