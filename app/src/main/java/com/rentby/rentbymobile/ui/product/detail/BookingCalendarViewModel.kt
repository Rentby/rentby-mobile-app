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
import com.rentby.rentbymobile.helper.calculateDay
import com.rentby.rentbymobile.ui.order.OrderActivity
import java.util.Calendar

class BookingCalendarViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _productMock = MutableLiveData<ProductMock>()
    val productMock: LiveData<ProductMock> get() = _productMock

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _rentStart = MutableLiveData<Long>()
    val rentStart: LiveData<Long> get() = _rentStart

    private val _rentEnd = MutableLiveData<Long>()
    val rentEnd: LiveData<Long> get() = _rentEnd

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> get() = _duration

    private val _rentTotal = MutableLiveData<Int>()
    val rentTotal: LiveData<Int> get() = _rentTotal

    private val _rentPrice = MutableLiveData<String>()
    val rentPrice: LiveData<String> get() = _rentPrice

    init {
        val tomorrow = tomorrow()
        _rentStart.value = tomorrow
        _rentEnd.value = tomorrow
        _rentPrice.value = "0"
        calculateDurationAndTotal()
    }

    fun getProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Fetch the product from the repository
            val fetchedProduct = productRepository.getProductById(productId)
            _productMock.value = fetchedProduct!!
            _rentPrice.value = fetchedProduct.price
            calculateDurationAndTotal()
            _isLoading.value = false
        }
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
