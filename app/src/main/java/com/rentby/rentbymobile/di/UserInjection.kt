package com.rentby.rentbymobile.di

import android.content.Context
import com.rentby.rentbymobile.data.pref.UserPreference
import com.rentby.rentbymobile.data.pref.dataStore
import com.rentby.rentbymobile.data.repository.UserRepository
import com.rentby.rentbymobile.data.retrofit.ApiConfig
import com.rentby.rentbymobile.data.retrofit.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserInjection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}