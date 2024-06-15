package com.rentby.rentbymobile.ui.product.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val product = productRepository.getProductById(productId)
                if (product != null) {
                    _product.postValue(product)
                } else {
                    _toastMessage.postValue("Product not found")
                }
            } catch (e: Exception) {
                Log.e("DetailProductViewModel", "Error getting product", e)
                _toastMessage.postValue("Error getting product")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
