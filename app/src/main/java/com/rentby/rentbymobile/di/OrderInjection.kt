package com.rentby.rentbymobile.di

import com.rentby.rentbymobile.data.repository.OrderRepository
import com.rentby.rentbymobile.data.retrofit.ApiConfig

object OrderInjection {
    fun provideRepository(): OrderRepository {
        val apiService = ApiConfig.getApiService()
        return OrderRepository.getInstance(apiService)
    }
}