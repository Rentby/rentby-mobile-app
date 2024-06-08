package com.rentby.rentbymobile.ui.order

import OrderList.getMockOrders
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rentby.rentbymobile.data.model.Order

class OrderDetailViewModel : ViewModel() {
    private val _orderDetail = MutableLiveData<Order?>()
    val orderDetail: LiveData<Order?> = _orderDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mockOrders = getMockOrders()

    fun getOrderDetail(orderId: String) {
        _isLoading.value = true

        // Simulate loading and getting data
        val order = mockOrders.find { it.id == orderId }
        _orderDetail.value = order
        _isLoading.value = false
    }
}