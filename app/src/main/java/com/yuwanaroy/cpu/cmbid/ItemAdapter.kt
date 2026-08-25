package com.yuwanaroy.cpu.cmbid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuwanaroy.cpu.cmbid.model.ClickPoint

class ItemAdapter(
    private val list: MutableList<ClickPoint>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvXy: TextView = itemView.findViewById(R.id.tvXy)
        val btnDelete: View = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.item_click_point, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvXy.text = "X: ${item.x} Y: ${item.y} Delay: ${item.delay}ms"
        holder.btnDelete.setOnClickListener { onDelete(position) }
    }

    override fun getItemCount(): Int = list.size
}