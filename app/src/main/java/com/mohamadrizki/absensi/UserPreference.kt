package com.mohamadrizki.absensi

import android.content.Context
import android.util.Log
import com.mohamadrizki.absensi.data.model.LoggedInUser

class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val USER_ID = "user_id"
        private const val NAME = "name"
        private const val IS_LOGGED_IN = "is_logged_in"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: LoggedInUser) {
        val editor = preferences.edit()
        editor.putString(USER_ID, value.userId)
        editor.putString(NAME, value.displayName)
        editor.putBoolean(IS_LOGGED_IN, value.isLoggedIn)
        editor.apply()
    }

    fun getUser(): LoggedInUser {
        val user = LoggedInUser()
        user.userId = preferences.getString(USER_ID, "")
        user.displayName = preferences.getString(NAME, "")
        user.isLoggedIn = preferences.getBoolean(IS_LOGGED_IN, false)

        return user
    }
}