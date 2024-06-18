package com.rentby.rentbymobile.ui.seller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.data.model.Seller
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.repository.SellerRepository
import com.rentby.rentbymobile.data.response.SellerDetailResponse
import com.rentby.rentbymobile.data.response.SellerProductResponse
import com.rentby.rentbymobile.data.response.toOrderEstimation
import com.rentby.rentbymobile.data.response.toProductItem
import com.rentby.rentbymobile.data.response.toSeller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerProfileViewModel(
    private val productRepository: ProductRepository,
    private val sellerRepository: SellerRepository
) : ViewModel() {

    private val _seller = MutableLiveData<Seller?>()
    val seller: MutableLiveData<Seller?> = _seller

    private val _sellerId = MutableLiveData<String>("")

    val products: LiveData<PagingData<ProductItem>> = _sellerId.switchMap { sellerID ->
        if (sellerID.isNotEmpty()) {
            productRepository.getSellerProducts(sellerID)
                .cachedIn(viewModelScope)
        } else {
            MutableLiveData<PagingData<ProductItem>>(PagingData.empty())
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun getSeller(sellerId: String) {
        _isLoading.value = true
        val client = sellerRepository.getSellerByIdApi(sellerId)
        client.enqueue(object : Callback<SellerDetailResponse> {
            override fun onResponse(
                call: Call<SellerDetailResponse>,
                response: Response<SellerDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val sellerDetailResponse = response.body()
                    if (sellerDetailResponse != null) {
                        val sellerData = sellerDetailResponse.toSeller()
                        _seller.postValue(sellerData)
                        setSellerId(sellerId)
                    }
                }
            }

            override fun onFailure(call: Call<SellerDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _seller.postValue(
                    Seller("", "", "", "", 0, "", "",
                    )
                )
            }
        })
    }

    fun setSellerId(sellerId: String) {
        _sellerId.value = sellerId
    }
}