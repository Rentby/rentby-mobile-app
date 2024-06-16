package com.rentby.rentbymobile.ui.main

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
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
    private val _category = MutableLiveData<String>("hiking")

    private lateinit var state: Parcelable
    fun saveRecyclerViewState(parcelable: Parcelable) { state = parcelable }
    fun restoreRecyclerViewState() : Parcelable = state
    fun stateInitialized() : Boolean = ::state.isInitialized

    val products: LiveData<PagingData<ProductItem>> = _category.switchMap { category ->
        productRepository.getProductsByCategory(category)
            .cachedIn(viewModelScope)
    }

    fun setCategory(category: String) {
        _category.value = category
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
                    val name = userDetail.data?.name.toString()
                    val address = userDetail.data?.address.toString()
                    val phoneNumber = userDetail.data?.phoneNumber.toString()
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

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}