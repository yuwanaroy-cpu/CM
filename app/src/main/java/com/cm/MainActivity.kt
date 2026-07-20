package com.cm.autobid

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cm.autobid.databinding.ActivityMainBinding

data class Point(val min: Int, val max: Int, val jarak: Int) // Data class buat nyimpen 1 aturan

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isRunning = false
    private val listPoint = ArrayList<Point>() // List buat nyimpen semua pengaturan
    private lateinit var adapter: PointAdapter // Adapter RecyclerView

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
            val interval = binding.etInterval.text.toString()
            val delay = binding.etDelay.text.toString()
            val min = binding.etMin.text.toString().toIntOrNull()?: 0
            val max = binding.etMax.text.toString().toIntOrNull()?: 0
            val jarak = binding.etJarak.text.toString().toIntOrNull()?: 0

            if(min == 0 || max == 0) {
                Toast.makeText(this, "Isi Harga Minimal & Maksimal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            listPoint.add(Point(min, max, jarak)) // Tambah ke list
            adapter.notifyDataSetChanged() // Update RecyclerView

            // Kosongkan field
            binding.etMin.text.clear()
            binding.etMax.text.clear()
            binding.etJarak.text.clear()

            Toast.makeText(this, "Pengaturan Tersimpan!", Toast.LENGTH_SHORT).show()
        }

        binding.btnStart.setOnClickListener {
            isRunning =!isRunning
            if (isRunning) {
                binding.tvStatus.text = "RUNNING"
                binding.tvStatus.setTextColor(resources.getColor(android.R.color.holo_green_light))
                binding.btnStart.text = " STOP"
                binding.btnStart.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_pause, 0, 0, 0)
                binding.btnStart.backgroundTintList = resources.getColorStateList(android.R.color.holo_red_dark)
            } else {
                binding.tvStatus.text = "STOP"
                binding.tvStatus.setTextColor(resources.getColor(android.R.color.holo_red_light))
                binding.btnStart.text = " MULAI"
                binding.btnStart.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0)
                binding.btnStart.backgroundTintList = resources.getColorStateList(android.R.color.holo_green_dark)
            }
        }
    }
}

// Adapter sederhana buat nampilin list
class PointAdapter(private val list: ArrayList<Point>) : androidx.recyclerview.widget.RecyclerView.Adapter<PointAdapter.ViewHolder>() {
    class ViewHolder(val binding: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding)
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        (holder.binding as android.widget.TextView).text = "Rp${item.min} - Rp${item.max} | ${item.jarak} Meter"
        holder.binding.setPadding(16,16,16,16)
    }
    override fun getItemCount(): Int = list.size
}
