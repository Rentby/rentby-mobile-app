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

class DetailProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _product = MutableLiveData<Product?>()
    val product: MutableLiveData<Product?> = _product

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getProduct(productId: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val product = repository.getProductById(productId)
            _product.postValue(product)
            _isLoading.postValue(false)
        }
    }
}