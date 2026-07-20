package com.cm


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cm.databinding.ItemPointBinding
import com.cm.model.Point


class ItemAdapter(
    private val items:ArrayList<Point>
):
RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


    class ViewHolder(
        val binding: ItemPointBinding
    ):
    RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType:Int
    ):ViewHolder {


        val binding =
            ItemPointBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )


        return ViewHolder(binding)
    }



    override fun onBindViewHolder(
        holder:ViewHolder,
        position:Int
    ){

        val item = items[position]


        holder.binding.tvItem.text =
    "Rp${item.minHarga} - Rp${item.maxHarga} | ${item.jarak} Meter"
    }



    override fun getItemCount():Int{
        return items.size
    }

}
