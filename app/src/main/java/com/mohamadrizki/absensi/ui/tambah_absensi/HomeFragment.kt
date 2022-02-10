package com.mohamadrizki.absensi.ui.tambah_absensi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.mohamadrizki.absensi.App
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.UserPreference
import com.mohamadrizki.absensi.databinding.FragmentHomeBinding
import com.mohamadrizki.absensi.databinding.FragmentTambahAbsensiBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private val userPreference = UserPreference(App.applicationContext())

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("d MMMM yyyy")
        val tanggal = sdf.format(Date())
        val nama = userPreference.getUser().displayName
        val jam = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        binding.tvNama.text = nama
        binding.tvDate.text = tanggal

        if (jam in 16..21) {
            binding.ibAbsen.setImageResource(R.drawable.tombol_absen_keluar)
        }

        binding.ibAbsen.setOnClickListener {
            val mCategoryFragment = TambahAbsensiFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment_activity_main, mCategoryFragment, TambahAbsensiFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

}