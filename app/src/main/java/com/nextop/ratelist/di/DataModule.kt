package com.nextop.ratelist.di

import com.nextop.ratelist.data.repository.ExchangeRateRepository
import com.nextop.ratelist.data.repository.ExchangeRateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun provideCurrencyRateRepo(currencyRateRepositoryImpl: ExchangeRateRepositoryImpl):ExchangeRateRepository
}