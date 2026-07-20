package com.cm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cm.databinding.ItemRowBinding

class PointAdapter(
    private val list: ArrayList<Point>
) : RecyclerView.Adapter<PointAdapter.ViewHolder>() {


    class ViewHolder(
        val binding: ItemRowBinding
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item = list[position]

        holder.binding.tvItem.text =
            "Rp ${item.min} - Rp ${item.max} | ${item.jarak} Meter"
    }


    override fun getItemCount(): Int {
        return list.size
    }
}
