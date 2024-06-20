package com.rentby.rentbymobile.data.response

import com.google.gson.annotations.SerializedName

data class OrderDetailResponse(

	@field:SerializedName("snap_token")
	val snapToken: String? = null,

	@field:SerializedName("pickup_location")
	val pickupLocation: String? = null,

	@field:SerializedName("rent_total")
	val rentTotal: Int? = null,

	@field:SerializedName("lateDuration")
	val lateDuration: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("isRated")
	val isRated: String? = null,

	@field:SerializedName("rent_price")
	val rentPrice: Int? = null,

	@field:SerializedName("service_fee")
	val serviceFee: Int? = null,

	@field:SerializedName("rent_end")
	val rentEnd: RentEnd? = null,

	@field:SerializedName("order_time")
	val orderTime: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("rent_start")
	val rentStart: RentStart? = null,

	@field:SerializedName("seller_name")
	val sellerName: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("return_time")
	val returnTime: String? = null,

	@field:SerializedName("lateChange")
	val lateChange: String? = null,

	@field:SerializedName("deposit")
	val deposit: Int? = null,

	@field:SerializedName("order_total")
	val orderTotal: Int? = null,

	@field:SerializedName("pickup_time")
	val pickupTime: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("payment_method")
	val paymentMethod: String? = null,

	@field:SerializedName("seller_id")
	val sellerId: String? = null,

	@field:SerializedName("rent_duration")
	val rentDuration: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RentEnd(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)

data class RentStart(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)
