package com.cm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cm.databinding.ActivityMainBinding

data class Point(val min: Int, val max: Int, val jarak: Int)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isRunning = false
    private val listPoint = ArrayList<Point>()
    private lateinit var adapter: PointAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        adapter = PointAdapter(listPoint)
        binding.rvPoint.layoutManager = LinearLayoutManager(this)
        binding.rvPoint.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnAddPoint.setOnClickListener {
            val min = binding.etMin.text.toString().toIntOrNull()?: 0
            val max = binding.etMax.text.toString().toIntOrNull()?: 0
            val jarak = binding.etJarak.text.toString().toIntOrNull()?: 0

            if(min == 0 || max == 0) {
                Toast.makeText(this, "Isi Harga Minimal & Maksimal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            listPoint.add(Point(min, max, jarak))
            adapter.notifyItemInserted(listPoint.size - 1)

            binding.etMin.text.clear()
            binding.etMax.text.clear()
            binding.etJarak.text.clear()

            Toast.makeText(this, "Pengaturan Tersimpan!", Toast.LENGTH_SHORT).show()
        }

        binding.btnStart.setOnClickListener {
            isRunning =!isRunning
            if (isRunning) {
                binding.tvStatus.text = "RUNNING"
                binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_light))
                binding.btnStart.text = " STOP"
                binding.btnStart.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_pause, 0, 0, 0)
                binding.btnStart.backgroundTintList = getColorStateList(android.R.color.holo_red_dark)
            } else {
                binding.tvStatus.text = "STOP"
                binding.tvStatus.setTextColor(getColor(android.R.color.holo_red_light))
                binding.btnStart.text = " MULAI"
                binding.btnStart.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0)
                binding.btnStart.backgroundTintList = getColorStateList(android.R.color.holo_green_dark)
            }
        }
    }
}

class PointAdapter(private val list: ArrayList<Point>) : RecyclerView.Adapter<PointAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(android.R.id.text1)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tv.text = "Rp${item.min} - Rp${item.max} | ${item.jarak} Meter"
        holder.tv.setPadding(16,16,16,16)
    }
    override fun getItemCount(): Int = list.size
}
