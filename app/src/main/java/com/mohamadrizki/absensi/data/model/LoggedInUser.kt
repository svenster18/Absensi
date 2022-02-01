package com.mohamadrizki.absensi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Parcelize
data class LoggedInUser(
    var nip: String? = null,
    var username: String? = null,
    var displayName: String? = null,
    var no_telp: String? = null,
    var jabatan: String? = null,
    var isLoggedIn: Boolean = false
) : Parcelable