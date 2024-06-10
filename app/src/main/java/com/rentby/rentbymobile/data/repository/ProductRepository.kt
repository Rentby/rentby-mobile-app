package com.rentby.rentbymobile.data.repository

import android.util.Log
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.data.model.Product

class ProductRepository {
    fun getProductById(productId: String): Product? {
        return ProductList.getProducts().find { it.id == productId }
    }
}