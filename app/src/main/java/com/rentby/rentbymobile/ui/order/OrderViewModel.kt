package com.rentby.rentbymobile.ui.order

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Booking
import com.rentby.rentbymobile.data.model.OrderEstimation
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.model.ProductMock
import com.rentby.rentbymobile.data.repository.BookingRepository
import com.rentby.rentbymobile.data.repository.OrderRepository
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.response.EstimateOrderResponse
import com.rentby.rentbymobile.data.response.ProductDetailResponse
import com.rentby.rentbymobile.data.response.toOrderEstimation
import com.rentby.rentbymobile.data.response.toProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) : ViewModel()  {
    private val bookingRepository = BookingRepository()

    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> = _product

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isOrderLoading = MutableLiveData<Boolean>()
    val isOrderLoading: LiveData<Boolean> = _isOrderLoading

    private val _orderId = MutableLiveData<String>()
    val orderId: LiveData<String> = _orderId

    private val _estimateOrderResponse = MutableLiveData<OrderEstimation>()
    val estimateOrderResponse: LiveData<OrderEstimation> = _estimateOrderResponse

    private val _startOrderDetailActivity = MutableLiveData<Intent?>()
    val startOrderDetailActivity: LiveData<Intent?> = _startOrderDetailActivity

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun estimateOrder(productId: String, rentStart: Long, rentEnd: Long) {
        _isOrderLoading.value = true
        viewModelScope.launch {
            orderRepository.estimateOrder(
                productId = productId,
                rentStart = rentStart,
                rentEnd = rentEnd,
                onResult = {
                    if (it != null) {
                        _estimateOrderResponse.postValue(it.toOrderEstimation())
                    }
                    _isOrderLoading.value = false
                },
                onError = {
                    _error.postValue(it?.message)
                    _isOrderLoading.value = false
                }
            )
        }
    }

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
                _product.postValue(
                    Product("", "", 0, "", 0.0f, 0, "", "")
                )
            }
        })
    }

    fun generateBookingData(productId: String, rentStart: Long, rentEnd: Long){
        Log.d("OrderViewModel", "start ${rentStart.toString()} end ${rentEnd.toString()}")
//        _booking.postValue(bookingRepository.makeBooking(productId, rentStart, rentEnd))
    }

    fun makeOrder(productId: String, userId: String, rentStart: Long, rentEnd: Long){
        _isOrderLoading.value = true
        viewModelScope.launch {
            orderRepository.makeOrder(
                productId = productId,
                userId = userId,
                rentStart = rentStart,
                rentEnd = rentEnd,
                onResult = {
                    if (it != null) {
                        _orderId.value = it.orderId.toString()
                        Log.d("75755", it.orderId.toString())
                    }
                    _isOrderLoading.value = false
                },
                onError = {
                    _error.postValue(it?.message)
                    _isOrderLoading.value = false
                }
            )
        }
    }
}