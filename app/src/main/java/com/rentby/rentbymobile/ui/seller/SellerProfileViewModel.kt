package com.rentby.rentbymobile.ui.seller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.model.Seller
import com.rentby.rentbymobile.data.repository.SellerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SellerProfileViewModel : ViewModel() {
    val repository = SellerRepository()

    private val _seller = MutableLiveData<Seller?>()
    val seller: MutableLiveData<Seller?> = _seller

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun getSeller(sellerId: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val seller = repository.getSellerById(sellerId)
                if (seller != null) {
                    _seller.postValue(seller)
                } else {
                    _toastMessage.postValue("Seller not found")
                }
            } catch (e: Exception) {
                Log.e("DetailProductViewModel", "Error getting seller", e)
                _toastMessage.postValue("Error getting seller")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}