package com.rentby.rentbymobile.ui.main

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rentby.rentbymobile.data.model.OrderItem
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.OrderRepository
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _category = MutableLiveData<String>("hiking")

    private val _query = MutableLiveData<String>("")
    val query: LiveData<String> = _query

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _orders = MutableLiveData<List<OrderItem>>()
    val orders: LiveData<List<OrderItem>> = _orders

    private lateinit var state: Parcelable
    fun saveRecyclerViewState(parcelable: Parcelable) { state = parcelable }
    fun restoreRecyclerViewState() : Parcelable = state
    fun stateInitialized() : Boolean = ::state.isInitialized

    val products: LiveData<PagingData<ProductItem>> = _category.switchMap { category ->
        productRepository.getProductsByCategory(category)
            .cachedIn(viewModelScope)
    }

    val searchProducts: LiveData<PagingData<ProductItem>> = _query.switchMap { query ->
        if (query.isNotEmpty()) {
            productRepository.getProductsByQuery(query)
                .cachedIn(viewModelScope)
        } else {
            MutableLiveData<PagingData<ProductItem>>(PagingData.empty())
        }
    }

    fun setCategory(category: String) {
        _category.value = category
    }

    fun setQuery(query: String) {
        _query.value = query
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun isRegistered(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val userDetail = userRepository.getUserDetail(email)
                if (userDetail != null) {
                    val name = userDetail.name.toString()
                    val address = userDetail.address.toString()
                    val phoneNumber = userDetail.phoneNumber.toString()
                    userRepository.saveSession(UserModel(email, name, address, phoneNumber))
                    onResult(true)
                } else {
                    _loading.value = false
                    onResult(false)
                }
            } catch (e: Exception) {
                _loading.value = false
                onResult(false)
            }
        }
    }

    fun getUserOrder(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            orderRepository.getUserOrder(
                userId,
                onResult = {
                    _orders.postValue(it)
                    _isLoading.value = false
                },
                onError = {
                    _isLoading.value = false
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}