package com.nextop.ratelist.data.repository

import com.nextop.ratelist.data.remote.ExchangeRateResponse
import retrofit2.Response

interface ExchangeRateRepository {
    suspend fun getExchangeRates():Response<ExchangeRateResponse>
}