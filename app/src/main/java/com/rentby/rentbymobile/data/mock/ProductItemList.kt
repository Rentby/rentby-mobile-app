package com.rentby.rentbymobile.data.mock

import com.rentby.rentbymobile.data.model.ProductItem

object ProductItemList {
    fun getProducts(): ArrayList<ProductItem> {
        return arrayListOf(
            ProductItem(
                "1",
                "Tenda Dome Kapasitas 4 Orang Waterproof Portable",
                "50000",
                4.8f,
                "",
            ),
            ProductItem(
                "2",
                "Kompor Portable Mini",
                "25000",
                4.5f,
                ""
            ),
            ProductItem(
                "3",
                "Tas Ransel Gunung Carrier 50L Backsupport Water Resistant",
                "40000",
                4.7f,
                ""
            ),
            ProductItem(
                "4",
                "Sleeping Bag Waterproof dan Compact",
                "15000",
                4.6f,
                ""
            ),
            ProductItem(
                "5",
                "Matras Gulung Camping Portable",
                "10000",
                4.2f,
                ""
            ),
            ProductItem(
                "6",
                "Lampu Tenda LED Rechargeable",
                "20000",
                4.3f,
                ""
            ),
            ProductItem(
                "7",
                "Alat Masak Portable Set untuk Camping",
                "30000",
                4.5f,
                ""
            ),
            ProductItem(
                "8",
                "Jas Hujan Ponco Ultralight",
                "12000",
                4.1f,
                ""
            ),
            ProductItem(
                "9",
                "Trekking Pole Adjustable Aluminium",
                "18000",
                4.4f,
                ""
            ),
            ProductItem(
                "10",
                "Filter Air Portable untuk Mendaki",
                "35000",
                4.8f,
                ""
            )
        )
    }
}

