package com.mohamadrizki.absensi.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.data.model.Absensi
import com.mohamadrizki.absensi.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val list = ArrayList<Absensi>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAbsensi.setHasFixedSize(true)

        list.addAll(listAbsensi)
        showRecyclerList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val listAbsensi: ArrayList<Absensi>
        get() {
            val dataTanggal = resources.getStringArray(R.array.data_tanggal)
            val dataJamMasuk = resources.getStringArray(R.array.data_jam_masuk)
            val dataJamKeluar = resources.getStringArray(R.array.data_jam_keluar)
            val listAbsensi = ArrayList<Absensi>()
            for (i in dataTanggal.indices) {
                val absensi = Absensi(dataTanggal[i], dataJamMasuk[i], dataJamKeluar[i])
                listAbsensi.add(absensi)
            }
            return listAbsensi
        }

    private fun showRecyclerList() {
        binding.rvAbsensi.layoutManager = LinearLayoutManager(activity)
        val listAbsensiAdapter = ListAbsensiAdapter(list)
        binding.rvAbsensi.adapter = listAbsensiAdapter
    }
}