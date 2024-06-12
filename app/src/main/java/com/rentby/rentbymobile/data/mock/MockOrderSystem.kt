package com.rentby.rentbymobile.data.mock

object MockOrderSystem {
    fun makeOrder(
        productId: String,
        userId: String,
        rent_start: String,
        rent_end: String
    ) :String {
        return "ORD1"
    }
}