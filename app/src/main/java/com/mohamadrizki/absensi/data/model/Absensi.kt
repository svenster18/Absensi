package com.mohamadrizki.absensi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Absensi(
    var tanggal: String,
    var jamMasuk: String,
    var jamKeluar: String
) : Parcelable
