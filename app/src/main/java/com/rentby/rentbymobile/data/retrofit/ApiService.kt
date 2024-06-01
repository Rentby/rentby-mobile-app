package com.rentby.rentbymobile.data.retrofit

import com.rentby.rentbymobile.data.request.RegisterRequest
import com.rentby.rentbymobile.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("user-register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse
}