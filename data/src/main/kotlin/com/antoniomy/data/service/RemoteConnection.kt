package com.antoniomy.data.service

import com.antoniomy.data.timeOut
import com.antoniomy.data.urlCities
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RemoteConnection {
    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(timeOut, TimeUnit.SECONDS)
        .readTimeout(timeOut, TimeUnit.SECONDS)
        .writeTimeout(timeOut, TimeUnit.SECONDS)
        .build()

    private val builder: Retrofit = Retrofit.Builder()
        .baseUrl(urlCities)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val remoteService: RemoteService = builder.create(RemoteService::class.java)
}