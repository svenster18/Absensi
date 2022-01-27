package com.mohamadrizki.absensi.ui.login

import androidx.lifecycle.LiveData
import com.mohamadrizki.absensi.data.model.LoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LiveData<LoggedInUser>? = null,
    val error: Int? = null
)