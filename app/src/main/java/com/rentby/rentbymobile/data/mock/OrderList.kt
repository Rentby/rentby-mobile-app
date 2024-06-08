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
                productName = "Tenda Dome Kapasitas 4 Orang Waterproof Portable",
                rentStart = "2024-06-05",
                rentEnd = "2024-06-10",
                rentPrice = "50000",
                pickupLocation = "123 Main Street",
                serviceFee = "1000",
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
                productName = "Kompor Portable Mini",
                rentStart = "2024-06-06",
                rentEnd = "2024-06-11",
                rentPrice = "25000",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                serviceFee = "1000",
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
                productName = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                rentStart = "2024-06-07",
                rentEnd = "2024-06-12",
                rentPrice = "40000",
                pickupLocation = "789 Sample Road",
                serviceFee = "1000",
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
                productName = "Sleeping Bag Waterproof dan Compact",
                rentStart = "2024-06-08",
                rentEnd = "2024-06-13",
                rentPrice = "15000",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                serviceFee = "1000",
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
                productName = "Matras Gulung Camping Portable",
                rentStart = "2024-06-09",
                rentEnd = "2024-06-14",
                rentPrice = "10000",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                serviceFee = "1000",
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
                productName = "Lampu Tenda LED Rechargeable",
                rentStart = "2024-06-10",
                rentEnd = "2024-06-15",
                rentPrice = "20000",
                pickupLocation = "Jl. Racing Centre No.18, Karampuang, Kec. Panakkukang, Kota Makassar, Sulawesi Selatan 90231",
                serviceFee = "1000",
                isRated = true,
                image = R.drawable.lampu
            )
        )
    }
}
