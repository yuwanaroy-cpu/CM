package com.yuwanaroy.cpu.cmbid // <-- GANTI INI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuwanaroy.cpu.cmbid.databinding.ItemRowBinding // <-- GANTI INI
import com.yuwanaroy.cpu.cmbid.model.Point // <-- GANTI INI
import java.util.ArrayList

class PointAdapter(private val list: ArrayList<Point>) :
    RecyclerView.Adapter<PointAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvItem.text = "I:${item.interval}ms D:${item.delay}ms | Rp${item.minHarga}-${item.maxHarga} | ${item.jarak}m"

        // TAMBAHIN INI BIAR BISA HAPUS PAS DI KLIK
        holder.binding.root.setOnClickListener {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }
    }

    override fun getItemCount(): Int = list.size
}
