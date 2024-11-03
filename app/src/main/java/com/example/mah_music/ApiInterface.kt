package com.example.mah_music

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @Headers(
        "x-rapidapi-key: 3399cd3726mshdf11839d45533a1p11885ajsn36eef3ce5887",
        "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com")
             //"User-Agent: Retrofit")
    @GET("search")
    fun getData(@Query("q") query: String): Call<MyData?>
}