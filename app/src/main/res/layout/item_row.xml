package com.yuwanaroy.cpu.cmbid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuwanaroy.cpu.cmbid.databinding.ItemRowBinding
import com.yuwanaroy.cpu.cmbid.model.Point

class ItemAdapter(
    private val list: MutableList<Point>, 
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.VH>() {

    inner class VH(val b: ItemRowBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(p.context), p, false)
        return VH(binding)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = list[pos]
        h.b.tvInterval.text = "${item.interval} ms" // SESUAI LAYOUT KAMU
        h.b.btnDelete.setOnClickListener {
            listener(pos)
        }
    }

    override fun getItemCount() = list.size
}
