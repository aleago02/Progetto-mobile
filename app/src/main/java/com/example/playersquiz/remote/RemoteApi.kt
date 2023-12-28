package com.example.playersquiz.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RemoteApi {
    private const val BASE_URL = "https://api-football-v1.p.rapidapi.com"
    private const val BASEN_URL = "https://free-nba.p.rapidapi.com"
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

    private val retro = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASEN_URL)
        .build()

    val apiNbaService: ApiNbaService by lazy{
        retro.create(ApiNbaService::class.java)
    }

    val apiService: ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }

}