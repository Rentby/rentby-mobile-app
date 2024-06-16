package com.rentby.rentbymobile.di

import com.rentby.rentbymobile.data.repository.SellerRepository
import com.rentby.rentbymobile.data.retrofit.ApiConfig

object SellerInjection {
    fun provideRepository(): SellerRepository {
        val apiService = ApiConfig.getApiService()
        return SellerRepository.getInstance(apiService)
    }
}