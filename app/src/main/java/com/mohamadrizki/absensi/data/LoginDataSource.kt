package com.mohamadrizki.absensi.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mohamadrizki.absensi.App
import com.mohamadrizki.absensi.UserPreference
import com.mohamadrizki.absensi.data.model.LoggedInUser
import com.mohamadrizki.absensi.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    val userPreference = UserPreference(App.applicationContext())

    fun login(username: String, password: String): Result<LiveData<LoggedInUser>> {
        return try {
            // TODO: handle loggedInUser authentication
            val user = MutableLiveData<LoggedInUser>()
            val client = ApiConfig.getApiService().login(username, password)
            client.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val userResponse = response.body()?.data
                        val loggedInUser = LoggedInUser(userResponse?.username, userResponse?.nama, true)
                        userPreference.setUser(loggedInUser)
                        user.value = loggedInUser
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            Result.Success(user)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
        userPreference.setUser(LoggedInUser(isLoggedIn = false))
    }

    companion object {
        const val TAG = "LoginDataSource"
    }
}