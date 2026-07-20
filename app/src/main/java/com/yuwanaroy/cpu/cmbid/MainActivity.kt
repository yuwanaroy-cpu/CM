package com.yuwanaroy.cpu.cmbid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yuwanaroy.cpu.cmbid.service.CMForegroundService
import com.yuwanaroy.cpu.cmbid.service.FloatingService

class MainActivity : AppCompatActivity() {

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    // Minta izin notifikasi Android 13+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Izin Notifikasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // nanti kita bikin layoutnya

        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        // 1. Minta izin Notifikasi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // 2. Cek Izin Overlay
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
            Toast.makeText(this, "Mohon aktifkan izin Tampilkan di atas aplikasi lain", Toast.LENGTH_LONG).show()
        }

        // 3. Cek Accessibility
        if (!isAccessibilityServiceEnabled()) {
            Toast.makeText(this, "Mohon aktifkan Layanan Aksesibilitas CM BID", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        btnStart.setOnClickListener {
            startService(Intent(this, CMForegroundService::class.java))
            startService(Intent(this, FloatingService::class.java))
            Toast.makeText(this, "CM BID Dijalankan", Toast.LENGTH_SHORT).show()
        }

        btnStop.setOnClickListener {
            stopService(Intent(this, CMForegroundService::class.java))
            stopService(Intent(this, FloatingService::class.java))
            Toast.makeText(this, "CM BID Dihentikan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val expectedComponentName = "$packageName/.service.CMAccessibilityService"
        val services = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        return services?.contains(expectedComponentName) == true
    }
}            } else {
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
