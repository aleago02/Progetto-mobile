package com.example.playersquiz.remote

import com.example.playersquiz.remote.models.ResponseRootMetadataRemoteModel
import retrofit2.http.GET
import retrofit2.http.Headers

interface PlayerRemoteService {

    @Headers(
        "X-RapidAPI-Key: 09dc0d4a1fmsh7d4827d9bec8e79p120211jsn11687f421b43",
        "X-RapidAPI-Host: api-football-v1.p.rapidapi.com"
    )
    @GET("/v3/transfers?player=254")
    suspend fun getMetadata(): ResponseRootMetadataRemoteModel
}