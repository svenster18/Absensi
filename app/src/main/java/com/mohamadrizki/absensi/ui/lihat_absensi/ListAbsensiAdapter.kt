package com.mohamadrizki.absensi.ui.lihat_absensi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.data.model.Absensi
import com.mohamadrizki.absensi.data.model.AbsensiItem

class ListAbsensiAdapter(private val listAbsensi: List<AbsensiItem>): RecyclerView.Adapter<ListAbsensiAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_absensi, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val tanggal = listAbsensi[position].tanggal
        val jammasuk = listAbsensi[position].jammasuk
        val jamkeluar = listAbsensi[position].jamkeluar
        holder.tvTanggal.text = tanggal
        holder.tvJamMasuk.text = jammasuk
        holder.tvJamKeluar.text = jamkeluar

        holder.itemView.setOnClickListener { onItemClickCallBack.onItemClicked(listAbsensi[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listAbsensi.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTanggal: TextView = itemView.findViewById(R.id.tv_item_tanggal)
        var tvJamMasuk: TextView = itemView.findViewById(R.id.tv_item_jam_masuk)
        var tvJamKeluar: TextView = itemView.findViewById(R.id.tv_item_jam_keluar)
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: AbsensiItem)
    }
}