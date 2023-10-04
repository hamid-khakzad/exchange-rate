package com.nextop.ratelist.ui.exchangerate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.nextop.ratelist.R
import com.nextop.ratelist.data.remote.ExchangeRateResponse
import com.nextop.ratelist.data.repository.ExchangeRateRepository
import com.nextop.ratelist.util.NetworkUtil
import com.nextop.ratelist.util.Resource
import com.nextop.ratelist.util.StringResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val stringResourcesProvider: StringResourcesProvider,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {
    private val _exchangeRate = MutableLiveData<Resource<ExchangeRateResponse>>()
    val exchangeRate: LiveData<Resource<ExchangeRateResponse>> = _exchangeRate

    init {
        getExchangeRate()
    }

    fun getExchangeRate() = viewModelScope.launch {
        safeExchangeRateCall()
    }

    private suspend fun safeExchangeRateCall() {
        _exchangeRate.postValue(Resource.Loading())
        try {
            if (networkUtil.hasInternetConnection()) {
                val exchangeRateResponse = exchangeRateRepository.getExchangeRates()
                _exchangeRate.postValue(handleExchangeRateResponse(exchangeRateResponse))
            } else {
                _exchangeRate.postValue(Resource.Error(stringResourcesProvider.getString(R.string.no_connectivity_error)))
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> _exchangeRate.postValue(
                    Resource.Error(
                        stringResourcesProvider.getString(
                            R.string.network_failure_error
                        )
                    )
                )

                is JsonSyntaxException -> {
                    _exchangeRate.postValue(Resource.Error(stringResourcesProvider.getString(R.string.invalid_data_error)))
                }

                else -> _exchangeRate.postValue(Resource.Error(stringResourcesProvider.getString(R.string.unkown_error)))
            }
        }
    }

    private fun handleExchangeRateResponse(response: Response<ExchangeRateResponse>): Resource<ExchangeRateResponse> {
        if (response.isSuccessful) {
            response.body()?.let { exchangeRateResponse ->
                return Resource.Success(exchangeRateResponse)
            }
        }
        return Resource.Error(stringResourcesProvider.getString(R.string.server_error))
    }
}