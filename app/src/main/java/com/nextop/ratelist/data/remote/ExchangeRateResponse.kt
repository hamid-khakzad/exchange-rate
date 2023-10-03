package com.nextop.ratelist.data.remote

import com.nextop.ratelist.data.local.ExchangeRate

data class ExchangeRateResponse(
    val rates: List<ExchangeRate>
) {
}