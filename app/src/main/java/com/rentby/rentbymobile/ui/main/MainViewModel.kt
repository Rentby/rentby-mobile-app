package com.rentby.rentbymobile.ui.main

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.repository.UserRepository
import com.rentby.rentbymobile.data.response.ProductListResponse
import com.rentby.rentbymobile.data.response.ResultsItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private lateinit var state: Parcelable
    fun saveRecyclerViewState(parcelable: Parcelable) { state = parcelable }
    fun restoreRecyclerViewState() : Parcelable = state
    fun stateInitialized() : Boolean = ::state.isInitialized

    val products: LiveData<PagingData<ProductItem>> by lazy {
        productRepository.getProductsByCategory("hiking").cachedIn(viewModelScope)
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}