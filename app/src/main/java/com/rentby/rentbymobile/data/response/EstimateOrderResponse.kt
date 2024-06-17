package com.rentby.rentbymobile.data.response

import com.google.gson.annotations.SerializedName
import com.rentby.rentbymobile.data.model.OrderEstimation

data class EstimateOrderResponse(

	@field:SerializedName("rent_start")
	val rentStart: String? = null,

	@field:SerializedName("rent_total")
	val rentTotal: Int? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("rent_price")
	val rentPrice: Int? = null,

	@field:SerializedName("service_fee")
	val serviceFee: Int? = null,

	@field:SerializedName("deposit")
	val deposit: Int? = null,

	@field:SerializedName("rent_end")
	val rentEnd: String? = null,

	@field:SerializedName("order_total")
	val orderTotal: Int? = null,

	@field:SerializedName("product_name")
	val productName: String? = null
)

fun EstimateOrderResponse.toOrderEstimation(): OrderEstimation {
	return OrderEstimation(
		productId = this.productId ?: "",
		productName = this.productName ?: "",
		rentStart = this.rentStart ?: "0000-00-00",
		rentEnd = this.rentEnd ?: "0000-00-00",
		rentPrice = this.rentPrice ?: 0,
		rentTotal = this.rentTotal ?: 0,
		deposit = this.deposit ?: 0,
		serviceFee = this.serviceFee ?: 0,
		orderTotal = this.orderTotal ?: 0
	)
}
