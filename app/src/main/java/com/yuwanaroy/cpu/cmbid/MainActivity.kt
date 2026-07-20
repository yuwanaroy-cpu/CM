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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuwanaroy.cpu.cmbid.databinding.ActivityMainBinding
import com.yuwanaroy.cpu.cmbid.model.Point
import com.yuwanaroy.cpu.cmbid.service.CMAccessibilityService
import com.yuwanaroy.cpu.cmbid.service.CMForegroundService
import com.yuwanaroy.cpu.cmbid.service.FloatingService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val pointList = ArrayList<Point>()
    private lateinit var adapter: ItemAdapter
    private var isRunning = false

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
        val filter = IntentFilter("ACTION_STOP_CM_BID")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(stopReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(stopReceiver, filter)
        }
    }

    override fun onStop() {
        super.onStop()
        // FIX: BUNGKUS TRY CATCH BIAR ANTI CRASH
        try {
            unregisterReceiver(stopReceiver)
        } catch (_: IllegalArgumentException) {
            // Receiver sudah tidak terdaftar, abaikan
        }
    }

    private fun setupRecyclerView() {
        adapter = ItemAdapter(pointList) { pos ->
            pointList.removeAt(pos)
            adapter.notifyItemRemoved(pos)
        }
        binding.rvPoint.layoutManager = LinearLayoutManager(this)
        binding.rvPoint.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnAddPoint.setOnClickListener {
            val interval = binding.etInterval.text.toString().toLongOrNull() ?: 0L
            val delay = binding.etDelay.text.toString().toLongOrNull() ?: 0L
            val minHarga = binding.etMin.text.toString().toIntOrNull() ?: 0
            val maxHarga = binding.etMax.text.toString().toIntOrNull() ?: 0
            val jarak = binding.etJarak.text.toString().toIntOrNull() ?: 0

            if (interval > 0 && delay > 0 && minHarga > 0 && maxHarga > 0) {
                pointList.add(Point(0,0, interval, delay, minHarga, maxHarga, jarak))
                adapter.notifyItemInserted(pointList.size - 1)
                Toast.makeText(this, "Pengaturan Disimpan", Toast.LENGTH_SHORT).show()
                binding.etInterval.text.clear()
                binding.etDelay.text.clear()
                binding.etMin.text.clear()
                binding.etMax.text.clear()
                binding.etJarak.text.clear()
            } else {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            }
        }

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
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Izinkan Tampilkan di atas aplikasi lain dulu", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            isRunning = false; updateStatus("STOP"); return
        }
        if (CMAccessibilityService.instance == null) {
            Toast.makeText(this, "Aktifkan CM BID di Aksesibilitas dulu", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            isRunning = false; updateStatus("STOP"); return
        }
        val fgIntent = Intent(this, CMForegroundService::class.java).apply { action = CMForegroundService.ACTION_START }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(fgIntent) else startService(fgIntent)
        startService(Intent(this, FloatingService::class.java))
        CMAccessibilityService.instance?.startService(pointList)
    }

    private fun stopAllService() {
        CMAccessibilityService.instance?.stopService()
        startService(Intent(this, CMForegroundService::class.java).apply { action = CMForegroundService.ACTION_STOP })
        stopService(Intent(this, FloatingService::class.java))
    }

    private fun updateStatus(status: String) {
        binding.tvStatus.text = status
        if (status == "RUNNING") {
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            binding.btnStart.text = "STOP"
            binding.btnStart.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_red_dark)
        } else {
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            binding.btnStart.text = "MULAI"
            binding.btnStart.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_green_dark)
        }
    }
}
