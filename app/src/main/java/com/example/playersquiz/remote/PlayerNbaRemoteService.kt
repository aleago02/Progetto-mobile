package com.example.playersquiz.remote

import com.example.playersquiz.remote.models.playernba.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiNbaService {

    @Headers(
        "Accept: application/json",
        "x-rapidapi-key: 09dc0d4a1fmsh7d4827d9bec8e79p120211jsn11687f421b43",
        "x-rapidapi-host: free-nba.p.rapidapi.com"
    )
    @GET("players/{id}")
    fun getPlayers(@Path("id") player: Int): Call<Data>
}