package com.rentby.rentbymobile.data.repository

import com.rentby.rentbymobile.data.mock.SellerList
import com.rentby.rentbymobile.data.model.Seller
import com.rentby.rentbymobile.data.response.SellerDetailResponse
import com.rentby.rentbymobile.data.retrofit.ApiService
import retrofit2.Call

class SellerRepository(private val apiService: ApiService) {
    fun getSellerById(sellerId: String): Seller? {
        return SellerList.getSeller().find { it.id == sellerId }
    }

    fun getSellerByIdApi(SellerId: String): Call<SellerDetailResponse> {
        return apiService.getSellerDetails(SellerId)
    }

    companion object {
        @Volatile
        private var instance: SellerRepository? = null

        fun getInstance(apiService: ApiService): SellerRepository {
            return instance ?: synchronized(this) {
                instance ?: SellerRepository(apiService).also { instance = it }
            }
        }
    }
}