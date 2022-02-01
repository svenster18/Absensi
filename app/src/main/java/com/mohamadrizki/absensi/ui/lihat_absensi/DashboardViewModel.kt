package com.mohamadrizki.absensi.ui.lihat_absensi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohamadrizki.absensi.App
import com.mohamadrizki.absensi.UserPreference
import com.mohamadrizki.absensi.data.ApiConfig
import com.mohamadrizki.absensi.data.model.Absensi
import com.mohamadrizki.absensi.data.model.AbsensiItem
import com.mohamadrizki.absensi.data.model.AbsensiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {

    private val userPreference = UserPreference(App.applicationContext())
    private val nip = userPreference.getUser().nip

    private val _listAbsensi = MutableLiveData<List<AbsensiItem>>()
    val listAbsensi: LiveData<List<AbsensiItem>> = _listAbsensi

    init {
        findListAbsensi()
    }

    private fun findListAbsensi() {
        val client = ApiConfig.getApiService().getAbsensi(nip)
        client.enqueue(object: Callback<AbsensiResponse> {
            override fun onResponse(
                call: Call<AbsensiResponse>,
                response: Response<AbsensiResponse>
            ) {
                if (response.isSuccessful) {
                    _listAbsensi.value = response.body()?.absensi
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AbsensiResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "DashboardViewModel"
    }
}