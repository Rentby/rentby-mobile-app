package com.rentby.rentbymobile.ui.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.model.Booking
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.repository.BookingRepository
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.helper.calculateDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel()  {
    private val bookingRepository = BookingRepository()
    private val productRepository = ProductRepository()

    private val _product = MutableLiveData<Product?>()
    val product: MutableLiveData<Product?> = _product

    private val _booking = MutableLiveData<Booking>()
    val booking: MutableLiveData<Booking> = _booking

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getProduct(productId: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val product = productRepository.getProductById(productId)
            _product.postValue(product)
            _isLoading.postValue(false)
        }
    }

    fun generateBookingData(productId: String, rentStart: Long, rentEnd: Long){
        Log.d("OrderViewModel", "start ${rentStart.toString()} end ${rentEnd.toString()}")
        _booking.postValue(bookingRepository.makeBooking(productId, rentStart, rentEnd))
    }
}