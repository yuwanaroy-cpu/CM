package com.yuwanaroy.cpu.cmbid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuwanaroy.cpu.cmbid.databinding.ItemRowBinding
import com.yuwanaroy.cpu.cmbid.model.Point

class ItemAdapter(private val list: MutableList<Point>, private val onDelete: (Int) -> Unit) : 
    RecyclerView.Adapter<ItemAdapter.VH>() {
    inner class VH(val b: ItemRowBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(p: ViewGroup, v: Int) = VH(ItemRowBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, pos: Int) {
        val i = list[pos]
        h.b.tvInterval.text = "I:${i.interval} D:${i.delay} H:${i.minHarga}-${i.maxHarga} J:${i.jarak}km"
        h.b.btnDelete.setOnClickListener { onDelete(pos) }
    }
    override fun getItemCount() = list.size
}
