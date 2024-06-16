package com.rentby.rentbymobile.data.retrofit

import com.rentby.rentbymobile.data.request.RegisterRequest
import com.rentby.rentbymobile.data.response.ProductListResponse
import com.rentby.rentbymobile.data.response.RegisterResponse
import com.rentby.rentbymobile.data.response.UserDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("user-register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @GET("user-detail/{email}")
    suspend fun getUserDetail(
        @Path("email") email: String
    ): UserDetailResponse

    @GET("search-category")
    fun searchCategory(
        @Query("category") category: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ProductListResponse>
}