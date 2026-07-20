package com.yuwanaroy.cpu.cmbid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuwanaroy.cpu.cmbid.databinding.ActivityMainBinding
import com.yuwanaroy.cpu.cmbid.model.Point
import com.yuwanaroy.cpu.cmbid.service.CMAccessibilityService
import com.yuwanaroy.cpu.cmbid.service.CMForegroundService
import com.yuwanaroy.cpu.cmbid.service.FloatingService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val pointList = ArrayList<Point>()
    private lateinit var adapter: PointAdapter
    private var isRunning = false

    // Receiver buat nerima broadcast dari FloatingService tombol STOP
    private val stopReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_STOP_CM_BID") {
                isRunning = false
                stopAllService()
                updateStatus("STOP")
                Toast.makeText(this@MainActivity, "Auto Click Dihentikan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        updateStatus("STOP")
    }

    override fun onStart() {
        super.onStart()
        // Daftar receiver pas activity jalan
        registerReceiver(stopReceiver, IntentFilter("ACTION_STOP_CM_BID"))
    }

    override fun onStop() {
        super.onStop()
        // Lepas receiver pas activity mati
        unregisterReceiver(stopReceiver)
    }

    private fun setupRecyclerView() {
        adapter = PointAdapter(pointList)
        binding.rvPoint.layoutManager = LinearLayoutManager(this)
        binding.rvPoint.adapter = adapter
    }

    private fun setupClickListeners() {
        // TOMBOL SIMPAN PENGATURAN
        binding.btnAddPoint.setOnClickListener {
            val interval = binding.etInterval.text.toString().toIntOrNull() ?: 0
            val delay = binding.etDelay.text.toString().toIntOrNull() ?: 0
            val minHarga = binding.etMin.text.toString().toIntOrNull() ?: 0
            val maxHarga = binding.etMax.text.toString().toIntOrNull() ?: 0
            val jarak = binding.etJarak.text.toString().toIntOrNull() ?: 0

            if (interval > 0 && delay > 0 && minHarga > 0 && maxHarga > 0) {
                val point = Point(
                    x = 0, 
                    y = 0, 
                    interval = interval, 
                    delay = delay, 
                    minHarga = minHarga, 
                    maxHarga = maxHarga, 
                    jarak = jarak
                )
                pointList.add(point)
                adapter.notifyItemInserted(pointList.size - 1)
                Toast.makeText(this, "Pengaturan Disimpan", Toast.LENGTH_SHORT).show()
                
                // Kosongin form
                binding.etInterval.text.clear()
                binding.etDelay.text.clear()
                binding.etMin.text.clear()
                binding.etMax.text.clear()
                binding.etJarak.text.clear()
            } else {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // TOMBOL MULAI / STOP
        binding.btnStart.setOnClickListener {
            if (pointList.isEmpty()) {
                Toast.makeText(this, "Tambah pengaturan dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            isRunning = !isRunning

            if (isRunning) {
                updateStatus("RUNNING")
                startAllService()
                Toast.makeText(this, "Auto Click Dimulai", Toast.LENGTH_SHORT).show()
            } else {
                updateStatus("STOP")
                stopAllService()
                Toast.makeText(this, "Auto Click Dihentikan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startAllService() {
        // 1. Cek izin Overlay
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Izinkan Tampilkan di atas aplikasi lain dulu", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            isRunning = false
            updateStatus("STOP")
            return
        }
        
        // 2. Cek izin Aksesibilitas
        if (CMAccessibilityService.instance == null) {
            Toast.makeText(this, "Aktifkan CM BID di Aksesibilitas dulu", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            isRunning = false
            updateStatus("STOP")
            return
        }

        // 3. Nyalakan Foreground Service - Biar gak ke kill
        val fgIntent = Intent(this, CMForegroundService::class.java)
        fgIntent.action = CMForegroundService.ACTION_START
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(fgIntent)
        } else {
            startService(fgIntent)
        }

        // 4. Nyalakan Floating Service - Tombol Melayang
        startService(Intent(this, FloatingService::class.java))

        // 5. Nyalakan AutoClick di Accessibility Service
        CMAccessibilityService.instance?.startService(pointList)
    }

    private fun stopAllService() {
        // 1. Matikan AutoClick
        CMAccessibilityService.instance?.stopService()
        
        // 2. Matikan Foreground Service
        val fgIntent = Intent(this, CMForegroundService::class.java)
        fgIntent.action = CMForegroundService.ACTION_STOP
        startService(fgIntent)

        // 3. Matikan Floating Service
        stopService(Intent(this, FloatingService::class.java))
    }

    private fun updateStatus(status: String) {
        binding.tvStatus.text = status
        if (status == "RUNNING") {
            binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_dark))
            binding.btnStart.text = "STOP"
            binding.btnStart.backgroundTintList = getColorStateList(android.R.color.holo_red_dark)
        } else {
            binding.tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
            binding.btnStart.text = "MULAI"
            binding.btnStart.backgroundTintList = getColorStateList(android.R.color.holo_green_dark)
        }
    }
}
