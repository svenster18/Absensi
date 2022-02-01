package com.mohamadrizki.absensi.data.model

import com.google.gson.annotations.SerializedName

data class AbsensiResponse(

	@field:SerializedName("absensi")
	val absensi: List<AbsensiItem>
)

data class AbsensiItem(

	@field:SerializedName("foto_masuk")
	val fotomasuk: String? = null,

	@field:SerializedName("latitude_masuk")
	val latitudemasuk: String? = null,

	@field:SerializedName("longitude_keluar")
	val longitudekeluar: String? = null,

	@field:SerializedName("NIP")
	val nIP: String? = null,

	@field:SerializedName("latitude_keluar")
	val latitudekeluar: String? = null,

	@field:SerializedName("ID_absen")
	val iDAbsen: String? = null,

	@field:SerializedName("jam_masuk")
	val jammasuk: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("foto_keluar")
	val fotokeluar: String? = null,

	@field:SerializedName("jam_keluar")
	val jamkeluar: String? = null,

	@field:SerializedName("longitude_masuk")
	val longitudemasuk: String? = null
)
