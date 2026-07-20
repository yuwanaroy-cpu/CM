package com.yuwanaroy.cpu.cmbid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuwanaroy.cpu.cmbid.service.CMForegroundService
import com.yuwanaroy.cpu.cmbid.service.FloatingService

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var etInterval: EditText
    private lateinit var etDelay: EditText
    private lateinit var etMin: EditText
    private lateinit var etMax: EditText
    private lateinit var etJarak: EditText
    private lateinit var btnAddPoint: Button
    private lateinit var btnStart: Button
    private lateinit var rvPoint: RecyclerView

    private var isRunning = false

    // Minta izin notifikasi Android 13+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // INI LAYOUT KAMU

        initViews()
        setupPermissions()
        setupClick()
    }

    private fun initViews() {
        tvStatus = findViewById(R.id.tvStatus)
        etInterval = findViewById(R.id.etInterval)
        etDelay = findViewById(R.id.etDelay)
        etMin = findViewById(R.id.etMin)
        etMax = findViewById(R.id.etMax)
        etJarak = findViewById(R.id.etJarak)
        btnAddPoint = findViewById(R.id.btnAddPoint)
        btnStart = findViewById(R.id.btnStart)
        rvPoint = findViewById(R.id.rvPoint)

        rvPoint.layoutManager = LinearLayoutManager(this)
        // Nanti kita isi adapter buat DAFTAR PENGATURAN
    }

    private fun setupPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
            Toast.makeText(this, "Aktifkan 'Tampilkan di atas aplikasi lain'", Toast.LENGTH_LONG).show()
        }

        if (!isAccessibilityServiceEnabled()) {
            Toast.makeText(this, "Aktifkan Layanan Aksesibilitas CM BID", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    private fun setupClick() {
        btnAddPoint.setOnClickListener {
            // Simpan setting ke SharedPreferences / List
            saveSettings()
            Toast.makeText(this, "Pengaturan Disimpan", Toast.LENGTH_SHORT).show()
        }

        btnStart.setOnClickListener {
            if (!isRunning) {
                startService(Intent(this, CMForegroundService::class.java))
                startService(Intent(this, FloatingService::class.java))
                isRunning = true
                tvStatus.text = "JALAN"
                tvStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark, theme))
                btnStart.text = "BERHENTI"
            } else {
                stopService(Intent(this, CMForegroundService::class.java))
                stopService(Intent(this, FloatingService::class.java))
                isRunning = false
                tvStatus.text = "STOP"
                tvStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark, theme))
                btnStart.text = "MULAI"
            }
        }
    }

    private fun saveSettings() {
        val interval = etInterval.text.toString().toIntOrNull() ?: 300
        val delay = etDelay.text.toString().toIntOrNull() ?: 150
        val min = etMin.text.toString().toIntOrNull() ?: 10000
        val max = etMax.text.toString().toIntOrNull() ?: 50000
        val jarak = etJarak.text.toString().toIntOrNull() ?: 1000

        val prefs = getSharedPreferences("cmbid_prefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("interval", interval)
            putInt("delay", delay)
            putInt("min", min)
            putInt("max", max)
            putInt("jarak", jarak)
            apply()
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val expectedComponentName = "$packageName/.service.CMAccessibilityService"
        val services = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        return services?.contains(expectedComponentName) == true
    }
}
