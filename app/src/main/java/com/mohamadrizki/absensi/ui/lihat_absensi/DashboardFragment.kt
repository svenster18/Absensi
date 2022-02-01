package com.mohamadrizki.absensi.ui.lihat_absensi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.data.model.Absensi
import com.mohamadrizki.absensi.data.model.AbsensiItem
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

        val dashboardViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel::class.java)
        dashboardViewModel.listAbsensi.observe(requireActivity(), { absensi ->
            setAbsensiData(absensi)
        })

        binding.rvAbsensi.setHasFixedSize(true)
    }

    private fun setAbsensiData(absensi: List<AbsensiItem>) {
        binding.rvAbsensi.layoutManager = LinearLayoutManager(activity)
        val listAbsensiAdapter = ListAbsensiAdapter(absensi)
        binding.rvAbsensi.adapter = listAbsensiAdapter

        listAbsensiAdapter.setOnItemClickCallback(object : ListAbsensiAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: AbsensiItem) {
                showSelectedAbsensi(data)
            }
        })
        listAbsensiAdapter.notifyDataSetChanged()
    }

    private fun showSelectedAbsensi(data: AbsensiItem) {
        val intent = Intent(context, DetailAbsensiActivity::class.java)
        intent.putExtra(DetailAbsensiActivity.EXTRA_ABSENSI, data)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}