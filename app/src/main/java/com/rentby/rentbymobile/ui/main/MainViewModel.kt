package com.rentby.rentbymobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.repository.UserRepository
import com.rentby.rentbymobile.data.response.ProductListResponse
import com.rentby.rentbymobile.data.response.ResultsItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _products = MutableLiveData<List<ProductItem>?>()
    val products: LiveData<List<ProductItem>?> = _products

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private var currentOffset = 0
    private val limit = 10 // Define the limit for each page

    init {
        loadMoreHikingProducts()
    }

    fun loadMoreHikingProducts() {
        _loading.value = true
        val client = productRepository.getHikingProducts(limit, currentOffset)
        client.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    if (productResponse != null) {
                        val productItems = productResponse.results?.filterNotNull()?.map { resultsItem ->
                            convertResultsItemToProductItem(resultsItem)
                        } ?: emptyList()

                        val currentProducts = _products.value?.toMutableList() ?: mutableListOf()
                        currentProducts.addAll(productItems)
                        _products.postValue(currentProducts)

                        // Increment the offset for the next page
                        currentOffset += limit
                    }
                }
            }

            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                _loading.value = false
            }
        })
    }

    private fun convertResultsItemToProductItem(resultsItem: ResultsItem): ProductItem {
        return ProductItem(
            id = resultsItem.productId ?: "",
            name = resultsItem.productName ?: "",
            price = resultsItem.rentPrice?.toString() ?: "0",
            rating = resultsItem.rating?.toFloatOrNull() ?: 0.0f,
            imageUrl = resultsItem.urlPhoto ?: ""
        )
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
