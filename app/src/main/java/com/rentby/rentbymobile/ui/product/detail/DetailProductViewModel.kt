package com.rentby.rentbymobile.ui.product.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.response.ProductDetailResponse
import com.rentby.rentbymobile.data.response.toProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> = _product

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun getProduct(productId: String) {
        _isLoading.value = true
        val client = productRepository.getProductByIdApi(productId)
        client.enqueue(object : Callback<ProductDetailResponse> {
            override fun onResponse(
                call: Call<ProductDetailResponse>,
                response: Response<ProductDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    if (productResponse != null) {
                        val data = productResponse.toProduct()
                        _product.postValue(data)
                    }
                }
            }

            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}
