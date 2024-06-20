package com.rentby.rentbymobile.data.retrofit

import com.rentby.rentbymobile.data.request.EstimateOrderRequest
import com.rentby.rentbymobile.data.request.MakeOrderRequest
import com.rentby.rentbymobile.data.request.RegisterRequest
import com.rentby.rentbymobile.data.response.EstimateOrderResponse
import com.rentby.rentbymobile.data.response.OrderDetailResponse
import com.rentby.rentbymobile.data.response.OrderIdResponse
import com.rentby.rentbymobile.data.response.OrderListResponse
import com.rentby.rentbymobile.data.response.OrderListResponseItem
import com.rentby.rentbymobile.data.response.ProductDetailResponse
import com.rentby.rentbymobile.data.response.ProductListResponse
import com.rentby.rentbymobile.data.response.RegisterResponse
import com.rentby.rentbymobile.data.response.SellerDetailResponse
import com.rentby.rentbymobile.data.response.SellerProductResponse
import com.rentby.rentbymobile.data.response.SuccessResponse
import com.rentby.rentbymobile.data.response.UserDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("user")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @GET("user/{email}")
    suspend fun getUserDetail(
        @Path("email") email: String
    ): UserDetailResponse

    @GET("search-category")
    suspend fun searchCategory(
        @Query("category") category: String,
        @Query("page") page: Int,
    ): ProductListResponse

    @GET("product/{product_id}")
    fun getProductDetails(@Path("product_id") productId: String): Call<ProductDetailResponse>

    @GET("seller/{seller_id}")
    fun getSellerDetails(@Path("seller_id") sellerId: String): Call<SellerDetailResponse>

    @Headers("Content-Type: application/json")
    @POST("estimate-order")
    fun estimateOrder(
        @Body request: EstimateOrderRequest
    ): Call<EstimateOrderResponse>

    @GET("search")
    suspend fun searchProduct(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): ProductListResponse

    @GET("product-seller/{seller_id}")
    suspend fun getSellerProducts(
        @Path("seller_id") sellerId: String,
        @Query("page") page: Int,
        ): SellerProductResponse

    @Headers("Content-Type: application/json")
    @POST("payment/order")
    fun makeOrder(
        @Body request: MakeOrderRequest
    ): Call<OrderIdResponse>

    @GET("order/{order_id}")
    fun getOrderDetail(
        @Path("order_id") orderId: String,
    ): Call<OrderDetailResponse>

    @GET("active-order/{user_id}")
    fun getUserOrder(
        @Path("user_id") userId: String
    ): Call<List<OrderListResponseItem>>

    @Headers("Content-Type: application/json")
    @POST("receive-product/{order_id}")
    fun setOrderReceived(
        @Path("order_id") orderId: String
    ): Call<SuccessResponse>

    @Headers("Content-Type: application/json")
    @POST("completed-order/{order_id}")
    fun setOrderCompleted(
        @Path("order_id") orderId: String
    ): Call<SuccessResponse>

    @Headers("Content-Type: application/json")
    @POST("cancel-order/{order_id}")
    fun setOrderCanceled(
        @Path("order_id") orderId: String
    ): Call<SuccessResponse>


}