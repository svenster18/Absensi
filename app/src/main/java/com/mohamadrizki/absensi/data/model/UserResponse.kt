package com.mohamadrizki.absensi.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Error")
	val error: String? = null,

	@field:SerializedName("Data")
	val data: Data? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("Nama")
	val nama: String? = null,

	@field:SerializedName("NIP")
	val nIP: String? = null,

	@field:SerializedName("No_tlp")
	val noTlp: String? = null,

	@field:SerializedName("Jabatan")
	val jabatan: String? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable
