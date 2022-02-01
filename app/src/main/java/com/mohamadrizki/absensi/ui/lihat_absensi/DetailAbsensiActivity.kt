package com.mohamadrizki.absensi.ui.lihat_absensi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.data.model.AbsensiItem
import com.mohamadrizki.absensi.databinding.ActivityDetailAbsensiBinding
import com.mohamadrizki.absensi.databinding.ActivityMainBinding
import com.mohamadrizki.absensi.ui.tambah_absensi.HomeFragment

class DetailAbsensiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAbsensiBinding

    private lateinit var absensi: AbsensiItem

    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        absensi = intent.getParcelableExtra<AbsensiItem>(EXTRA_ABSENSI) as AbsensiItem

        binding.tvTanggal.text = "Tanggal : ${absensi.tanggal}"
        binding.tvJamMasuk.text = "Jam Masuk : ${absensi.jammasuk}"
        binding.tvJamKeluar.text = "Jam Masuk : ${absensi.jamkeluar}"
        Glide.with(this)
            .load("$url${absensi.fotomasuk}")
            .into(binding.photocaptureMasuk)

        Glide.with(this)
            .load("$url${absensi.fotokeluar}")
            .into(binding.photocaptureKeluar)

        val mapMasukFragment =
            supportFragmentManager.findFragmentById(R.id.map_masuk) as SupportMapFragment?

        mapMasukFragment?.getMapAsync {
            map = it

            map?.addMarker(
                MarkerOptions().position(LatLng(absensi.latitudemasuk?.toDouble()!!, absensi.longitudemasuk?.toDouble()!!)).title("Lokasi Pegawai")
            )
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(absensi.latitudemasuk?.toDouble()!!, absensi.longitudemasuk?.toDouble()!!), DetailAbsensiActivity.DEFAULT_ZOOM.toFloat()))
        }

        val mapKeluarFragment =
            supportFragmentManager.findFragmentById(R.id.map_masuk) as SupportMapFragment?

        mapKeluarFragment?.getMapAsync {
            map = it

            map?.addMarker(
                MarkerOptions().position(LatLng(absensi.latitudekeluar?.toDouble()!!, absensi.longitudekeluar?.toDouble()!!)).title("Lokasi Pegawai")
            )
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(absensi.latitudekeluar?.toDouble()!!, absensi.longitudekeluar?.toDouble()!!), DetailAbsensiActivity.DEFAULT_ZOOM.toFloat()))
        }

    }

    companion object {
        const val EXTRA_ABSENSI = "extra_absensi"
        const val url = "http://absensi.backendresearch.com/img/"
        const val DEFAULT_ZOOM = 15
    }
}