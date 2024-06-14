package com.rentby.rentbymobile.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.rentby.rentbymobile.data.pref.UserPreference
import com.rentby.rentbymobile.data.repository.UserRepository
import com.rentby.rentbymobile.data.retrofit.ApiConfig
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

object UserInjection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}