package com.rentby.rentbymobile.data.repository

import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.data.mock.SellerList
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.data.model.Seller

class SellerRepository {
    fun getSellerById(sellerId: String): Seller? {
        return SellerList.getSeller().find { it.id == sellerId }
    }
}