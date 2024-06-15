package com.rentby.rentbymobile.di

import android.content.Context
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.retrofit.ApiConfig

object ProductInjection {
    fun provideRepository(): ProductRepository {
        val apiService = ApiConfig.getApiService()
        return ProductRepository.getInstance(apiService)
    }
}