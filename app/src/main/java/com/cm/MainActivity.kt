package com.cm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cm.databinding.ActivityMainBinding
import com.cm.model.Point
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val pointList = ArrayList<Point>()
    private lateinit var adapter: PointAdapter
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        updateStatus("STOP")
    }

    private fun setupRecyclerView() {
        adapter = PointAdapter(pointList)
        binding.rvPoint.layoutManager = LinearLayoutManager(this)
        binding.rvPoint.adapter = adapter
    }

    private fun setupClickListeners() {
        // Tombol SIMPAN PENGATURAN
        binding.btnAddPoint.setOnClickListener {
            val interval = binding.etInterval.text.toString().toIntOrNull() ?: 0
            val delay = binding.etDelay.text.toString().toIntOrNull() ?: 0
            val minHarga = binding.etMin.text.toString().toIntOrNull() ?: 0
            val maxHarga = binding.etMax.text.toString().toIntOrNull() ?: 0
            val jarak = binding.etJarak.text.toString().toIntOrNull() ?: 0

            if (interval > 0 && delay > 0 && minHarga > 0 && maxHarga > 0) {
                val point = Point(interval, delay, minHarga, maxHarga, jarak)
                pointList.add(point)
                adapter.notifyItemInserted(pointList.size - 1)
                Toast.makeText(this, "Pengaturan Disimpan", Toast.LENGTH_SHORT).show()
                
                // Kosongin input
                binding.etInterval.text.clear()
                binding.etDelay.text.clear()
                binding.etMin.text.clear()
                binding.etMax.text.clear()
                binding.etJarak.text.clear()
            } else {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol MULAI
        binding.btnStart.setOnClickListener {
            if (pointList.isEmpty()) {
                Toast.makeText(this, "Tambah pengaturan dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            isRunning = !isRunning
            if (isRunning) {
                updateStatus("RUNNING")
                Toast.makeText(this, "Auto Bid Dimulai", Toast.LENGTH_SHORT).show()
                // TODO: Jalankan service bid di sini
            } else {
                updateStatus("STOP")
                Toast.makeText(this, "Auto Bid Dihentikan", Toast.LENGTH_SHORT).show()
                // TODO: Hentikan service bid di sini
            }
        }
    }

    private fun updateStatus(status: String) {
        binding.tvStatus.text = status
        if (status == "RUNNING") {
            binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_dark))
            binding.btnStart.text = " STOP"
            binding.btnStart.backgroundTintList = getColorStateList(android.R.color.holo_red_dark)
        } else {
            binding.tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
            binding.btnStart.text = " MULAI"
            binding.btnStart.backgroundTintList = getColorStateList(android.R.color.holo_green_dark)
        }
    }
}
