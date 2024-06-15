package com.rentby.rentbymobile.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.paging.ProductCategoryPagingSource
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

//    fun getProductsByCategory(category: String): Pager<Int, ProductItem> {
//        return Pager(
//            config = PagingConfig(pageSize = 10),
//            pagingSourceFactory = { ProductCategoryPagingSource(apiService, category) }
//        )
//    }

    fun getProductsByCategory(category: String): LiveData<PagingData<ProductItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { ProductCategoryPagingSource(apiService, category) }
        ).liveData
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