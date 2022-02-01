package com.mohamadrizki.absensi.data

import com.mohamadrizki.absensi.data.model.AbsensiResponse
import com.mohamadrizki.absensi.data.model.PostAbsensiResponse
import com.mohamadrizki.absensi.data.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @Multipart
    @POST("absensi")
    fun tambahAbsensi(
        @Part("NIP") NIP: RequestBody,
        @Part("tanggal") tanggal: RequestBody,
        @Part fotomasuk: MultipartBody.Part,
        @Part("latitude_masuk") latitudemasuk: RequestBody,
        @Part("longitude_masuk") longitudemasuk: RequestBody
    ): Call<PostAbsensiResponse>

    @Multipart
    @POST("absenkeluar")
    fun absenKeluar(
        @Part("NIP") NIP: RequestBody,
        @Part("tanggal") tanggal: RequestBody,
        @Part("jam_keluar") jamkeluar: RequestBody,
        @Part fotokeluar: MultipartBody.Part,
        @Part("latitude_keluar") latitudekeluar: RequestBody,
        @Part("longitude_keluar") longitudekeluar: RequestBody
    ): Call<PostAbsensiResponse>

    @GET("absensi/{NIP}")
    fun getAbsensi(
        @Path("NIP") NIP: String?
    ): Call<AbsensiResponse>
}