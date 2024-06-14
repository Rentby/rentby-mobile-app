package com.rentby.rentbymobile.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[NAME_KEY] = user.name
            preferences[ADDRESS_KEY] = user.address
            preferences[PHONE_NUMBER_KEY] = user.phoneNumber
            preferences[IS_REGISTERED_KEY] = true
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[ADDRESS_KEY] ?: "",
                preferences[PHONE_NUMBER_KEY] ?: "",
                preferences[IS_REGISTERED_KEY] ?: false
            )
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val email = preferences[EMAIL_KEY] ?: ""
            val name = preferences[NAME_KEY] ?: ""
            val address = preferences[ADDRESS_KEY] ?: ""
            val phoneNumber = preferences[PHONE_NUMBER_KEY] ?: ""
            val isRegistered = preferences[IS_REGISTERED_KEY] ?: false
            UserModel(email, name, address, phoneNumber, isRegistered)
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        Log.d("TestingRegister", "User Pref Cleared")
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("name")
        private val ADDRESS_KEY = stringPreferencesKey("address")
        private val PHONE_NUMBER_KEY = stringPreferencesKey("phoneNumber")
        private val IS_REGISTERED_KEY = booleanPreferencesKey("isRegistered")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}