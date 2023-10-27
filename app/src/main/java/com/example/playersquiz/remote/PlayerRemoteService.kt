package com.example.playersquiz.remote
/*
    Questa deve essere un'interfaccia che viene poi implementata
    da un file a parte, che poi richiami nel fragment come ora richiamate sto file.
    Il file a parte conterrà praticamente quello che adesso avete
    per esempio in GameFragment.kt da riga 192 in poi, le funzioni di chiamata.
    Nel fragment(controller) si fa l'assegnazione dei dati presi dai repository e api(models)
    agli elementi degli xml(view), non si fanno le chiamate ai dati
*/
import com.example.playersquiz.remote.models.players.Players
import com.example.playersquiz.remote.models.transfer.MyData
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
    fun getTransfer(@Query("player") player: Long): Call<MyData>

    @Headers(
        "Accept: application/json",
        "x-rapidapi-key: 09dc0d4a1fmsh7d4827d9bec8e79p120211jsn11687f421b43",
        "x-rapidapi-host: api-football-v1.p.rapidapi.com"
    )
    @GET("v3/players")
    fun getPlayers(@Query("league") loague: Int, @Query("season") season: Int, @Query("page") page: Int): Call<Players>



}