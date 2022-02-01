package com.mohamadrizki.absensi.ui.tambah_absensi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.mohamadrizki.absensi.App
import com.mohamadrizki.absensi.UserPreference
import com.mohamadrizki.absensi.data.ApiConfig
import com.mohamadrizki.absensi.data.model.PostAbsensiResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.hours

class HomeViewModel : ViewModel() {

    val jam = Date().time.hours.inWholeHours

    private val _absenSukses = MutableLiveData(false)
    val absenSukses: LiveData<Boolean> = _absenSukses

    private val userPreference = UserPreference(App.applicationContext())

    val nip = userPreference.getUser().nip

    private var location: LatLng? = null
    private var photoFile: File? = null
    private var currentPhotoPath = ""

    fun setLatLng(location: LatLng) {
        this.location = location
    }

    fun getLatLng() = this.location

    fun setPhoto(photoFile: File) {
        this.photoFile = photoFile
    }

    fun getPhoto() = this.photoFile

    fun getCurrentPhotoPath() = this.currentPhotoPath

    fun setCurrentPhotoPath(currentPhotoPath: String) {
        this.currentPhotoPath = currentPhotoPath
    }

    fun absen() {
        val nipPart = RequestBody.create("text/plain".toMediaTypeOrNull(), nip!!)
        val filePart = MultipartBody.Part.createFormData("foto_masuk", photoFile?.name, RequestBody.create(
            "image/*".toMediaTypeOrNull(), photoFile!!))
        val latitudePart = RequestBody.create("text/plain".toMediaTypeOrNull(), location?.latitude.toString())
        val longitudePart = RequestBody.create("text/plain".toMediaTypeOrNull(), location?.longitude.toString())
        val client = ApiConfig.getApiService().tambahAbsensi(nipPart, filePart, latitudePart, longitudePart)
        client.enqueue(object: Callback<PostAbsensiResponse> {
            override fun onResponse(
                call: Call<PostAbsensiResponse>,
                response: Response<PostAbsensiResponse>
            ) {
                if (response.isSuccessful) {
                    _absenSukses.value = true
                }
            }

            override fun onFailure(call: Call<PostAbsensiResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun absenKeluar() {
        val nipPart = RequestBody.create("text/plain".toMediaTypeOrNull(), nip!!)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val sdfJam = SimpleDateFormat("HH:mm:ss")
        val tanggal = RequestBody.create("text/plain".toMediaTypeOrNull(), sdf.format(Date()))
        val jamkeluar = RequestBody.create("text/plain".toMediaTypeOrNull(), sdfJam.format(Date()))
        val filePart = MultipartBody.Part.createFormData("foto_keluar", photoFile?.name, RequestBody.create(
            "image/*".toMediaTypeOrNull(), photoFile!!))
        val latitudePart = RequestBody.create("text/plain".toMediaTypeOrNull(), location?.latitude.toString())
        val longitudePart = RequestBody.create("text/plain".toMediaTypeOrNull(), location?.longitude.toString())
        val client = ApiConfig.getApiService().absenKeluar(nipPart, tanggal, jamkeluar, filePart, latitudePart, longitudePart)
        client.enqueue(object: Callback<PostAbsensiResponse> {
            override fun onResponse(
                call: Call<PostAbsensiResponse>,
                response: Response<PostAbsensiResponse>
            ) {
                if (response.isSuccessful) {
                    _absenSukses.value = true
                }
            }

            override fun onFailure(call: Call<PostAbsensiResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}