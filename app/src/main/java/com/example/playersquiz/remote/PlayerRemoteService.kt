package com.example.playersquiz.remote

import com.example.playersquiz.remote.models.ResponseRootTransfersModel
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PlayerRemoteService {

    @Headers(
        "X-RapidAPI-Key: 09dc0d4a1fmsh7d4827d9bec8e79p120211jsn11687f421b43",
        "X-RapidAPI-Host: api-football-v1.p.rapidapi.com"
    )
    @GET("/v3/transfers")
    suspend fun getMetadata(@Query("player") player: Long): ResponseRootTransfersModel
}