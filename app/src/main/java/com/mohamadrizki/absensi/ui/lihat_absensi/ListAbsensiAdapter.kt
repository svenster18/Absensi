package com.mohamadrizki.absensi.ui.lihat_absensi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.data.model.Absensi

class ListAbsensiAdapter(private val listAbsensi: ArrayList<Absensi>): RecyclerView.Adapter<ListAbsensiAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_absensi, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (tanggal, jamMasuk, jamKeluar) = listAbsensi[position]
        holder.tvTanggal.text = tanggal
        holder.tvJamMasuk.text = jamMasuk
        holder.tvJamKeluar.text = jamKeluar
    }

    override fun getItemCount(): Int = listAbsensi.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTanggal: TextView = itemView.findViewById(R.id.tv_item_tanggal)
        var tvJamMasuk: TextView = itemView.findViewById(R.id.tv_item_jam_masuk)
        var tvJamKeluar: TextView = itemView.findViewById(R.id.tv_item_jam_keluar)
    }
}