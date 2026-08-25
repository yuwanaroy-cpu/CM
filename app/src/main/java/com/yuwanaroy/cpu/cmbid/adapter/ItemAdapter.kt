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
        var textWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_point, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val point = list[position]
        holder.tvPoint.text = "X: ${point.x.toInt()}, Y: ${point.y.toInt()}"

        // Lepas listener lama untuk mencegah bug saat recyclerview di-scroll
        holder.textWatcher?.let { holder.etDelay.removeTextChangedListener(it) }

        holder.etDelay.setText(point.delay.toString())

        // Buat TextWatcher baru
        val newWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newDelay = s.toString().toLongOrNull() ?: 1000L
                
                // Pastikan variabel 'delay' di data class/model 'ClickPoint' diubah menjadi 'var'!
                list[holder.bindingAdapterPosition].delay = newDelay 
                pref.saveClickPoints(list)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        holder.etDelay.addTextChangedListener(newWatcher)
        holder.textWatcher = newWatcher

        // Tombol Hapus
        holder.btnDelete.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                list.removeAt(currentPos)
                pref.saveClickPoints(list)
                notifyItemRemoved(currentPos)
                notifyItemRangeChanged(currentPos, list.size)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
