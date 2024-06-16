package com.rentby.rentbymobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
    val products: LiveData<PagingData<ProductItem>> by lazy {
        productRepository.getProductsByCategory("hiking").cachedIn(viewModelScope)
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

//    fun getHikingProducts() {
//        _loading.value = true
//        val client = productRepository.getHikingProduct()
//        client.enqueue(object : Callback<ProductListResponse> {
//            override fun onResponse(
//                call: Call<ProductListResponse>,
//                response: Response<ProductListResponse>
//            ) {
//                _loading.value = false
//                if (response.isSuccessful) {
//                    val productResponse = response.body()
//                    if (productResponse != null) {
//                        val productItems = productResponse.results?.filterNotNull()?.map { resultsItem ->
//                            convertResultsItemToProductItem(resultsItem)
//                        }
//                        _products.postValue(productItems)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
//                _loading.value = false
//            }
//        })
//    }

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