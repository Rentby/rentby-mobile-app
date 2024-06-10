package com.rentby.rentbymobile.data.repository

import com.rentby.rentbymobile.data.mock.BookingList
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.data.model.Booking
import com.rentby.rentbymobile.data.model.Product

class BookingRepository {
    fun makeBooking(productId: String, rentStart: Long, rentEnd: Long): Booking? {
        return BookingList.generateBooking(productId, rentStart, rentEnd)
    }
}