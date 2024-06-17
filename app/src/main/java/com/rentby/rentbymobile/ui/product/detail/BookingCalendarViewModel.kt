package com.rentby.rentbymobile.ui.product.detail

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.rentby.rentbymobile.data.model.ProductMock
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.google.android.material.datepicker.MaterialDatePicker
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.response.ProductDetailResponse
import com.rentby.rentbymobile.data.response.toProduct
import com.rentby.rentbymobile.helper.calculateDay
import com.rentby.rentbymobile.ui.order.OrderActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class BookingCalendarViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _rentStart = MutableLiveData<Long>()
    val rentStart: LiveData<Long> = _rentStart

    private val _rentEnd = MutableLiveData<Long>()
    val rentEnd: LiveData<Long> = _rentEnd

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> = _duration

    private val _rentTotal = MutableLiveData<Int>()
    val rentTotal: LiveData<Int> = _rentTotal

    private val _rentPrice = MutableLiveData<String>()
    val rentPrice: LiveData<String> = _rentPrice

    init {
        val tomorrow = tomorrow()
        _rentStart.value = tomorrow
        _rentEnd.value = tomorrow
        _rentPrice.value = "0"
        calculateDurationAndTotal()
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
                        _rentPrice.value = data.rentPrice.toString()
                        calculateDurationAndTotal()
                    }
                }
            }

            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun setRentDates(start: Long, end: Long) {
        _rentStart.value = start
        _rentEnd.value = end
        calculateDurationAndTotal()
    }

    private fun calculateDurationAndTotal() {
        val start = _rentStart.value ?: return
        val end = _rentEnd.value ?: return
        val price = _rentPrice.value?.toIntOrNull() ?: return

        val duration = calculateDay(start, end)
        _duration.value = duration
        _rentTotal.value = duration * price
    }

    fun today(): Long {
        return MaterialDatePicker.todayInUtcMilliseconds()
    }

    fun tomorrow(): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = today()
            add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.timeInMillis
    }

    fun makeBooking(context: Context, productId: String) {
        Log.d("OrderActivity", "Order Button Clicked - Product ID: $productId., Rent Start: ${rentStart.value.toString()}, Rent End: ${rentEnd.value.toString()}")
        val intent = Intent(context, OrderActivity::class.java).apply {
            putExtra(OrderActivity.PRODUCT_ID, productId)
            putExtra(OrderActivity.RENT_START, _rentStart.value)
            putExtra(OrderActivity.RENT_END, _rentEnd.value)
        }
        context.startActivity(intent)
    }
}
