package com.mohamadrizki.absensi.ui.lihat_gaji

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mohamadrizki.absensi.App
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.UserPreference
import com.mohamadrizki.absensi.data.ApiConfig
import com.mohamadrizki.absensi.data.model.AbsensiResponse
import com.mohamadrizki.absensi.databinding.FragmentDashboardBinding
import com.mohamadrizki.absensi.databinding.FragmentLihatGajiBinding
import com.mohamadrizki.absensi.ui.lihat_absensi.DashboardViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class LihatGajiFragment : Fragment() {

    private var _binding: FragmentLihatGajiBinding? = null
    private val binding get() = _binding!!

    private val userPreference = UserPreference(App.applicationContext())
    private val nip = userPreference.getUser().nip
    private val gaji = userPreference.getUser().gaji?.toDouble()!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLihatGajiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        tampilGaji()
    }

    private fun tampilGaji() {
        val client = ApiConfig.getApiService().getAbsensi(nip)
        client.enqueue(object: Callback<AbsensiResponse> {
            override fun onResponse(
                call: Call<AbsensiResponse>,
                response: Response<AbsensiResponse>
            ) {
                if (response.isSuccessful) {
                    val bulanIni = Calendar.getInstance().get(Calendar.MONTH)
                    val absensi = response.body()?.absensi
                    if (absensi != null) {
                        var tepatwaktu = 0
                        var jamlembur = 0
                        for(absen in absensi) {
                            val bulan = absen.tanggal?.substring(5,7)?.toInt()!!
                            if (bulan == bulanIni) {
                                val jamMasuk = absen.jammasuk?.substring(0,2)?.toInt()!!
                                var jamKeluar = 17
                                if(absen.jamkeluar != null && absen.jamkeluar?.substring(0,2)?.toInt()!! > 17) {
                                    jamKeluar = absen.jamkeluar?.substring(0,2)?.toInt()!!
                                    if (absen.jamkeluar?.substring(0,2)?.toInt()!! > 21) jamKeluar = 21
                                    jamlembur += jamKeluar - 17
                                }
                                if (jamMasuk < 9) tepatwaktu++
                            }
                        }
                        val tunjanganMakan = ((tepatwaktu.toDouble()/30)*0.1*gaji)
                        val tunjanganLembur = (jamlembur.toDouble()/30)*0.05*gaji
                        val totalGaji = gaji + tunjanganMakan + tunjanganLembur

                        binding.tvGajiPokok.text = "Gaji Pokok                 : Rp ${String.format("%.2f", gaji)}"
                        binding.tvTunjanganMakan.text = "Tunjangan Makan    : Rp ${String.format("%.2f", tunjanganMakan)}"
                        binding.tvTunjanganLembur.text = "Tunjangan Lembur   : Rp ${String.format("%.2f", tunjanganLembur)}"
                        binding.tvTotalGaji.text = "Total Gaji                    : Rp ${String.format("%.2f", totalGaji)}"
                        binding.progressBar.visibility = View.GONE
                    }
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
        private const val TAG = "LihatGajiFragment"
    }
}