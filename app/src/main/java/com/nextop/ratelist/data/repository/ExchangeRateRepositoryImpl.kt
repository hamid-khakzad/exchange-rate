package com.nextop.ratelist.data.repository

import com.nextop.ratelist.data.remote.ExchangeApi
import com.nextop.ratelist.data.remote.ExchangeRateResponse
import retrofit2.Response
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(private val exchangeApi: ExchangeApi) :
    ExchangeRateRepository {
    override suspend fun getExchangeRates(): Response<ExchangeRateResponse> =
        exchangeApi.getExchangeRates();
}