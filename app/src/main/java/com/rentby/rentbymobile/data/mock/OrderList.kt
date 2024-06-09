package com.rentby.rentbymobile.data.mock

import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Order

object OrderList {
    fun getMockOrders(): List<Order> {
        return listOf(
            Order(
                id = "ORD1",
                status = 1,
                orderTime = "2024-06-01 10:00",
                pickupTime = "",
                returnTime = "",
                payment = "GoPay",
                sellerId = "SELL1",
                sellerName = "John Doe",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                productName = "Tenda Dome Kapasitas 4 Orang Waterproof Portable",
                rentStart = "2024-06-05",
                rentEnd = "2024-06-10",
                rentDuration = 6, // 6 days
                rentPrice = 50000.0F,
                rentTotal = 300000.0F, // 50000 * 6
                deposit = 30000.0F, // 10% of 300000
                serviceFee = 1000.0F,
                lateDuration = 1,
                lateCharge = 0.0F,
                isRated = false,
                image = R.drawable.tenda
            ),
            Order(
                id = "ORD2",
                status = 2,
                orderTime = "2024-06-02 11:00",
                pickupTime = "",
                returnTime = "",
                payment = "GoPay",
                sellerId = "SELL2",
                sellerName = "Jane Smith",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                productName = "Kompor Portable Mini",
                rentStart = "2024-06-06",
                rentEnd = "2024-06-10",
                rentDuration = 5, // 5 days
                rentPrice = 25000.0F,
                rentTotal = 125000.0F, // 25000 * 5
                deposit = 12500.0F, // 10% of 125000
                serviceFee = 1000.0F,
                lateDuration = 1,
                lateCharge = 0.0F,
                isRated = true,
                image = R.drawable.kompor
            ),
            Order(
                id = "ORD3",
                status = 3,
                orderTime = "2024-06-03 09:00",
                pickupTime = "2024-06-07 10:00",
                returnTime = "",
                payment = "GoPay",
                sellerId = "SELL3",
                sellerName = "Emily Johnson",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                productName = "Tas Ransel Gunung Carrier 50L Backsupport Water Resistant",
                rentStart = "2024-06-07",
                rentEnd = "2024-06-08",
                rentDuration = 2, // 2 days
                rentPrice = 40000.0F,
                rentTotal = 80000.0F, // 40000 * 2
                deposit = 8000.0F, // 10% of 80000
                serviceFee = 1000.0F,
                lateDuration = 1,
                lateCharge = 0.0F,
                isRated = false,
                image = R.drawable.carrier
            ),
            Order(
                id = "ORD4",
                status = 4,
                orderTime = "2024-06-04 10:00",
                pickupTime = "2024-06-08 11:00",
                returnTime = "",
                payment = "GoPay",
                sellerId = "SELL4",
                sellerName = "Mike Johnson",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                productName = "Sleeping Bag Waterproof dan Compact",
                rentStart = "2024-06-08",
                rentEnd = "2024-06-08",
                rentDuration = 1, // 1 day
                rentPrice = 15000.0F,
                rentTotal = 15000.0F, // 15000 * 1
                deposit = 1500.0F, // 10% of 15000
                serviceFee = 1000.0F,
                lateDuration = 1,
                lateCharge = 15000.0F,
                isRated = false,
                image = R.drawable.sleeping_bag
            ),
            Order(
                id = "ORD5",
                status = 5,
                orderTime = "2024-06-05 12:00",
                pickupTime = "2024-06-09 13:00",
                returnTime = "2024-06-14 14:00",
                payment = "GoPay",
                sellerId = "SELL5",
                sellerName = "Jessica Brown",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                productName = "Matras Gulung Camping Portable",
                rentStart = "2024-06-09",
                rentEnd = "2024-06-13",
                rentDuration = 5, // 5 days
                rentPrice = 10000.0F,
                rentTotal = 50000.0F, // 10000 * 5
                deposit = 5000.0F, // 10% of 50000
                serviceFee = 1000.0F,
                lateDuration = 1,
                lateCharge = 0.0F,
                isRated = false,
                image = R.drawable.matras
            ),
            Order(
                id = "ORD6",
                status = 5,
                orderTime = "2024-06-06 14:00",
                pickupTime = "2024-06-10 15:00",
                returnTime = "2024-06-15 16:00",
                payment = "GoPay",
                sellerId = "SELL6",
                sellerName = "Andrew Williams",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                productName = "Lampu Tenda LED Rechargeable",
                rentStart = "2024-06-10",
                rentEnd = "2024-06-14",
                rentDuration = 5, // 5 days
                rentPrice = 20000.0F,
                rentTotal = 100000.0F, // 20000 * 5
                deposit = 10000.0F, // 10% of 100000
                serviceFee = 1000.0F,
                lateDuration = 1,
                lateCharge = 0.0F,
                isRated = true,
                image = R.drawable.lampu
            )
        )
    }
}
