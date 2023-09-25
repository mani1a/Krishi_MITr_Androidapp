package com.manila.fasaldoctor.chatgptApi.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CGApiUtils {

    // Chat GPT API

    fun getInstance() : CGApiInterface {
        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CGApiInterface::class.java)
    }
}