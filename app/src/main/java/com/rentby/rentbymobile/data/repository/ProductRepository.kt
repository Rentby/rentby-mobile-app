package com.rentby.rentbymobile.data.repository

import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.response.ProductListResponse
import com.rentby.rentbymobile.data.response.ResultsItem
import com.rentby.rentbymobile.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class ProductRepository(private val apiService: ApiService) {
    fun getProductById(productId: String): Product? {
        return ProductList.getProducts().find { it.id == productId }
    }

    fun getHikingProduct(): Call<ProductListResponse> {
        return apiService.searchCategory("hiking", 10, 0)
    }

    companion object {
        @Volatile
        private var instance: ProductRepository? = null

        fun getInstance(apiService: ApiService): ProductRepository {
            return instance ?: synchronized(this) {
                instance ?: ProductRepository(apiService).also { instance = it }
            }
        }
    }
}