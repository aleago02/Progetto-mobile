package com.example.playersquiz.remote



import com.example.playersquiz.remote.models.MyData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiService {


    @Headers(
        "Accept: application/json",
        "x-rapidapi-key: 09dc0d4a1fmsh7d4827d9bec8e79p120211jsn11687f421b43",
        "x-rapidapi-host: api-football-v1.p.rapidapi.com"
    )
    @GET("v3/transfers")
    fun getMetadata(@Query("player") player: Long): Call<MyData>

}