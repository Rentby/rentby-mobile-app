package com.rentby.rentbymobile.data.repository

import com.rentby.rentbymobile.data.request.EstimateOrderRequest
import com.rentby.rentbymobile.data.response.EstimateOrderResponse
import com.rentby.rentbymobile.data.retrofit.ApiService
import com.rentby.rentbymobile.helper.calculateDay
import com.rentby.rentbymobile.helper.millisecondsToDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository(private val apiService: ApiService) {

    fun estimateOrder(
        productId: String,
        rentStart: Long,
        rentEnd: Long,
        onResult: (EstimateOrderResponse?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        val request = EstimateOrderRequest(
            productId,
            millisecondsToDate(rentStart),
            millisecondsToDate(rentEnd),
            calculateDay(rentStart, rentEnd)
        )

        val call = apiService.estimateOrder(request)
        call.enqueue(object : Callback<EstimateOrderResponse> {
            override fun onResponse(
                call: Call<EstimateOrderResponse>,
                response: Response<EstimateOrderResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Throwable(response.errorBody()?.string()))
                }
            }

            override fun onFailure(call: Call<EstimateOrderResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    companion object {
        @Volatile
        private var instance: OrderRepository? = null

        fun getInstance(apiService: ApiService): OrderRepository {
            return instance ?: synchronized(this) {
                instance ?: OrderRepository(apiService).also { instance = it }
            }
        }
    }
}