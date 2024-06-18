package com.rentby.rentbymobile.ui.order

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.data.mock.OrderList.getMockOrders
import com.rentby.rentbymobile.data.model.OrderDetail
import com.rentby.rentbymobile.data.repository.OrderRepository
import com.rentby.rentbymobile.data.response.toOrderEstimation
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _orderDetailMock = MutableLiveData<Order?>()
    val orderDetailMock: LiveData<Order?> = _orderDetailMock

    private val _orderDetail = MutableLiveData<OrderDetail?>()
    val orderDetail: LiveData<OrderDetail?> = _orderDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mockOrders = getMockOrders()

    fun getOrderDetailMock(orderId: String) {
        _isLoading.value = true

        // Simulate loading and getting data
        val order = mockOrders.find { it.id == orderId }
        _orderDetailMock.value = order
        _isLoading.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOrderDetail(orderId: String) {
        _isLoading.value = true

        viewModelScope.launch {
            orderRepository.getOrderDetail(
                orderId,
                onResult = {
                    if (it != null) {
                        _orderDetail.postValue(OrderDetail.fromResponse(it))
                    }
                    _isLoading.value = false
                },
                onError = {
                    _isLoading.value = false
                }
            )
        }
    }
}