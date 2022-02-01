package com.mohamadrizki.absensi.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Absensi(
    val fotomasuk: String? = null,
    val latitudemasuk: String? = null,
    val longitudekeluar: String? = null,
    val nIP: String? = null,
    val latitudekeluar: String? = null,
    val iDAbsen: String? = null,
    val jammasuk: String? = null,
    val tanggal: String? = null,
    val fotokeluar: String? = null,
    val jamkeluar: String? = null,
    val longitudemasuk: String? = null
) : Parcelable
