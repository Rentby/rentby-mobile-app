package com.rentby.rentbymobile.data.repository

import android.util.Log
import androidx.lifecycle.asLiveData
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.pref.UserPreference
import com.rentby.rentbymobile.data.request.RegisterRequest
import com.rentby.rentbymobile.data.response.RegisterResponse
import com.rentby.rentbymobile.data.response.UserDetailResponse
import com.rentby.rentbymobile.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        Log.d("tstapi2", user.userId)
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {

        return userPreference.getSession()
    }

    suspend fun register(name: String, email: String, address: String, phoneNumber: String): RegisterResponse {
        Log.d("RegisterAPI", name + email + address + phoneNumber)
        val request = RegisterRequest(name, email, address, phoneNumber)
        return apiService.register(request)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getUserDetail(email: String): UserDetailResponse? {
        return try {
            apiService.getUserDetail(email)
        } catch (e: HttpException) {
            // Handle specific HTTP exceptions if needed
            null
        } catch (e: IOException) {
            // Handle network I/O exceptions if needed
            null
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}