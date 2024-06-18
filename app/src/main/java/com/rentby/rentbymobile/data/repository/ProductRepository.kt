package com.rentby.rentbymobile.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.model.ProductMock
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.paging.ProductListPagingSource
import com.rentby.rentbymobile.data.paging.ProductSearchPagingSource
import com.rentby.rentbymobile.data.paging.SellerProductListPagingSource
import com.rentby.rentbymobile.data.response.EstimateOrderResponse
import com.rentby.rentbymobile.data.response.ProductDetailResponse
import com.rentby.rentbymobile.data.response.ProductListResponse
import com.rentby.rentbymobile.data.response.ProductsItem
import com.rentby.rentbymobile.data.response.SellerProductResponse
import com.rentby.rentbymobile.data.response.toProduct
import com.rentby.rentbymobile.data.response.toProductItem
import com.rentby.rentbymobile.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(private val apiService: ApiService) {
    fun getProductById(productId: String): ProductMock? {
        return ProductList.getProducts().find { it.id == productId }
    }

    fun getProductByIdApi(productId: String): Call<ProductDetailResponse> {
        return apiService.getProductDetails(productId)
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

    fun getProductsByQuery(query: String): LiveData<PagingData<ProductItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { ProductSearchPagingSource(apiService, query) }
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

    fun getSellerProducts(sellerId: String) : LiveData<PagingData<ProductItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { SellerProductListPagingSource(apiService, sellerId) }
        ).liveData.map { pagingData ->
            pagingData.map { productsItem ->
                productsItem.toProductItem()
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