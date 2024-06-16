package com.rentby.rentbymobile.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.paging.ProductListPagingSource
import com.rentby.rentbymobile.data.response.ProductListResponse
import com.rentby.rentbymobile.data.response.ResultsItem
import com.rentby.rentbymobile.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.util.Locale.Category

class ProductRepository(private val apiService: ApiService) {
    fun getProductById(productId: String): Product? {
        return ProductList.getProducts().find { it.id == productId }
    }

//    fun getHikingProduct(): Call<ProductListResponse> {
//        return apiService.searchCategory("hiking", 1)
//    }

    fun getProductsByCategory(category: String): LiveData<PagingData<ProductItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { ProductListPagingSource(apiService, category) }
        ).liveData.map { pagingData ->
            pagingData.map { resultsItem ->
                ProductItem(
                    id = resultsItem.productId ?: "",
                    name = resultsItem.productName ?: "",
                    price = resultsItem.rentPrice?.toString() ?: "0",
                    rating = resultsItem.rating?.toFloatOrNull() ?: 0.0f,
                    imageUrl = resultsItem.urlPhoto ?: ""
                )
            }
        }
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