package com.example.playersquiz.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RemoteApi {

    private val BASE_URL = "https://api-football-v1.p.rapidapi.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    val playerRemoteService: PlayerRemoteService by lazy {
        retrofit.create(PlayerRemoteService::class.java)
    }
}