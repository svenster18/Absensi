package com.mohamadrizki.absensi.data.model

import com.google.gson.annotations.SerializedName

data class PostAbsensiResponse(

	@field:SerializedName("messages")
	val messages: Messages? = null,

	@field:SerializedName("error")
	val error: Any? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Messages(

	@field:SerializedName("success")
	val success: String? = null
)
