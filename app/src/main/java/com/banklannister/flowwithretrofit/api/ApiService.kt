package com.banklannister.flowwithretrofit.api

import com.banklannister.flowwithretrofit.response.ResponseCoinsMarkets
import com.banklannister.flowwithretrofit.response.ResponseDetailsCoin
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("coins/markets?sparkline=true")
    suspend fun getCoins(@Query("vs_currency") vs_currency: String):
            Response<ResponseCoinsMarkets>

    @GET("coins/{id}?sparkline=true")
    suspend fun getDetailCoin(@Path("id") id: String) : Response<ResponseDetailsCoin>
}