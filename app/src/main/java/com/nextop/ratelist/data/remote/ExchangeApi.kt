package com.nextop.ratelist.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ExchangeApi {
    @GET("code-challenge/index.php")
    suspend fun getExchangeRates():Response<ExchangeRateResponse>
}