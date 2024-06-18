package com.rentby.rentbymobile.ui.product.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.model.Seller
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.repository.SellerRepository
import com.rentby.rentbymobile.data.response.ProductDetailResponse
import com.rentby.rentbymobile.data.response.SellerDetailResponse
import com.rentby.rentbymobile.data.response.toProduct
import com.rentby.rentbymobile.data.response.toSeller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductViewModel(
    private val productRepository: ProductRepository,
    private val sellerRepository: SellerRepository
) : ViewModel() {
    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> = _product

    private val _seller = MutableLiveData<Seller>()
    val seller: LiveData<Seller> = _seller

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSellerLoading = MutableLiveData<Boolean>()
    val isSellerLoading: LiveData<Boolean> = _isSellerLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

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

                        getSeller(data.sellerId)
                    }
                }
            }

            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _product.postValue(
                    Product("", "", 0, "", 0.0f, 0, "", "")
                )

                _seller.postValue(
                    Seller("", "", "", "", 0, "", "")
                )
            }
        })
    }

    private fun getSeller(sellerId: String) {
        _isSellerLoading.value = true
        val client = sellerRepository.getSellerByIdApi(sellerId)
        client.enqueue(object : Callback<SellerDetailResponse> {
            override fun onResponse(
                call: Call<SellerDetailResponse>,
                response: Response<SellerDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _isSellerLoading.value = false
                    val sellerDetailResponse = response.body()
                    if (sellerDetailResponse != null) {
                        val sellerData = sellerDetailResponse.toSeller()
                        _seller.postValue(sellerData)
                    }
                }
            }

            override fun onFailure(call: Call<SellerDetailResponse>, t: Throwable) {
                _isSellerLoading.value = false
                _seller.postValue(
                    Seller("", "", "", "", 0, "", "",
                    )
                )
            }
        })
    }
}
