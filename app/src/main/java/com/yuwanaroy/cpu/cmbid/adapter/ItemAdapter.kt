package com.yuwanaroy.cpu.cmbid.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuwanaroy.cpu.cmbid.R
import com.yuwanaroy.cpu.cmbid.model.ClickPoint
import com.yuwanaroy.cpu.cmbid.utils.PreferenceManager

class ItemAdapter(
    private val list: MutableList<ClickPoint>,
    private val pref: PreferenceManager
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPoint: TextView = itemView.findViewById(R.id.tvPoint)
        val etDelay: EditText = itemView.findViewById(R.id.etDelay)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.item_point, parent, false) // pake item_point.xml punya kamu
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val point = list[position]
        holder.tvPoint.text = "X: ${point.x.toInt()}, Y: ${point.y.toInt()}"
        holder.etDelay.setText(point.delay.toString())

        // Update delay pas diketik
        holder.etDelay.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newDelay = s.toString().toLongOrNull()?: 1000L
                list[position].delay = newDelay
                pref.saveClickPoints(list) // langsung save
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Tombol Hapus
        holder.btnDelete.setOnClickListener {
            list.removeAt(position)
            pref.saveClickPoints(list)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }
    }

    override fun getItemCount(): Int = list.size
}