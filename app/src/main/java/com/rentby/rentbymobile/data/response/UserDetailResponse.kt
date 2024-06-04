package com.rentby.rentbymobile.data.response

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class Data(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)