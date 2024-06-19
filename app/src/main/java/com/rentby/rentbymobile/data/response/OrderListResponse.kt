package com.rentby.rentbymobile.data.response

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import com.rentby.rentbymobile.data.model.OrderItem
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// RentStart and RentEnd can be handled by a single class
data class RentTimestamp(
	@SerializedName("_seconds")
	val seconds: Long,
	@SerializedName("_nanoseconds")
	val nanoseconds: Int
)

data class OrderListResponse(
	@SerializedName("orderListResponse")
	val orderListResponse: List<OrderListResponseItem>
)

data class OrderListResponseItem(
	@SerializedName("order_id")
	val orderId: String,
	@SerializedName("product_name")
	val productName: String,
	@SerializedName("image_url")
	val imageUrl: String,
	@SerializedName("rent_start")
	val rentStart: RentTimestamp,
	@SerializedName("rent_end")
	val rentEnd: RentTimestamp,
	@SerializedName("order_total")
	val orderTotal: Int,
	@SerializedName("status")
	val status: String
)

@RequiresApi(Build.VERSION_CODES.O)
fun OrderListResponseItem.toOrderItem(): OrderItem {
	return OrderItem(
		status = this.status,
		orderId = this.orderId,
		productName = this.productName ?: "", // Assuming productName might be null or you need to handle it differently
		imageUrl = this.imageUrl ?: "", // Assuming imageUrl might be null or you need to handle it differently
		rentStart = convertRentTimestamp(this.rentStart),
		rentEnd = convertRentTimestamp(this.rentEnd),
		orderTotal = this.orderTotal
	)
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertRentTimestamp(rentTimestamp: RentTimestamp): String {
	val instant = Instant.ofEpochSecond(rentTimestamp.seconds, rentTimestamp.nanoseconds.toLong())
	val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
	return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}