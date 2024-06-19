package com.rentby.rentbymobile.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rentby.rentbymobile.data.repository.OrderRepository
import com.rentby.rentbymobile.data.repository.ProductRepository
import com.rentby.rentbymobile.data.repository.SellerRepository
import com.rentby.rentbymobile.data.repository.UserRepository
import com.rentby.rentbymobile.di.OrderInjection
import com.rentby.rentbymobile.di.ProductInjection
import com.rentby.rentbymobile.di.SellerInjection
import com.rentby.rentbymobile.di.UserInjection
import com.rentby.rentbymobile.ui.login.LoginViewModel
import com.rentby.rentbymobile.ui.main.MainViewModel
import com.rentby.rentbymobile.ui.order.OrderDetailViewModel
import com.rentby.rentbymobile.ui.order.OrderViewModel
import com.rentby.rentbymobile.ui.product.detail.BookingCalendarViewModel
import com.rentby.rentbymobile.ui.product.detail.DetailProductViewModel
import com.rentby.rentbymobile.ui.profile.ProfileViewModel
import com.rentby.rentbymobile.ui.register.RegisterViewModel
import com.rentby.rentbymobile.ui.seller.SellerProfileViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val sellerRepository: SellerRepository,
    private val orderRepository: OrderRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, productRepository, orderRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                OrderViewModel(productRepository, orderRepository) as T
            }
            modelClass.isAssignableFrom(BookingCalendarViewModel::class.java) -> {
                BookingCalendarViewModel(productRepository) as T
            }
            modelClass.isAssignableFrom(DetailProductViewModel::class.java) -> {
                DetailProductViewModel(productRepository, sellerRepository) as T
            }
            modelClass.isAssignableFrom(SellerProfileViewModel::class.java) -> {
                SellerProfileViewModel(productRepository, sellerRepository) as T
            }
            modelClass.isAssignableFrom(OrderDetailViewModel::class.java) -> {
                OrderDetailViewModel(orderRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    UserInjection.provideRepository(context),
                    ProductInjection.provideRepository(),
                    SellerInjection.provideRepository(),
                    OrderInjection.provideRepository()
                ).also { INSTANCE = it }
            }
        }
    }
}
