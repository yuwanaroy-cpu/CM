package com.yuwanaroy.cpu.cmbid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuwanaroy.cpu.cmbid.adapter.ItemAdapter
import com.yuwanaroy.cpu.cmbid.engine.ClickEngine
import com.yuwanaroy.cpu.cmbid.model.ClickPoint
import com.yuwanaroy.cpu.cmbid.service.FloatingService
import com.yuwanaroy.cpu.cmbid.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var pref: PreferenceManager
    private lateinit var adapter: ItemAdapter
    private var clickPoints = mutableListOf<ClickPoint>()
    private var isRunning = false

    // Terima data dari FloatingService
    private val pointReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "NEW_CLICK_POINT") {
                val x = intent.getFloatExtra("x", 0f)
                val y = intent.getFloatExtra("y", 0f)
                val newPoint = ClickPoint(x, y, 1000L)
                clickPoints.add(newPoint)
                pref.saveClickPoints(clickPoints)
                adapter.notifyItemInserted(clickPoints.size - 1)
                Toast.makeText(this@MainActivity, "Titik ditambah: X=${x.toInt()}, Y=${y.toInt()}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pref = PreferenceManager(this)
        clickPoints = pref.getClickPoints()

        ClickEngine.init(this) // wajib init dulu

        val btnAdd = findViewById<Button>(R.id.btnAddPoint)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)
        val rv = findViewById<RecyclerView>(R.id.rvPoints)

        // PENTING: adapter sekarang butuh 2 parameter
        adapter = ItemAdapter(clickPoints, pref)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // Tombol Tambah Titik -> buka FloatingService
        btnAdd.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
                startActivity(intent)
                Toast.makeText(this, "Izinkan 'Tampilkan di atas aplikasi lain'", Toast.LENGTH_LONG).show()
            } else {
                startService(Intent(this, FloatingService::class.java))
            }
        }

        // Tombol Start -> jalanin auto click
        btnStart.setOnClickListener {
            if (!isRunning) {
                if (clickPoints.isEmpty()) {
                    Toast.makeText(this, "Tambah titik dulu", Toast.LENGTH_SHORT).show()
                } else {
                    ClickEngine.startAutoClick(clickPoints)
                    isRunning = true
                    btnStart.text = "Running..."
                }
            }
        }

        // Tombol Stop
        btnStop.setOnClickListener {
            ClickEngine.stopAutoClick()
            isRunning = false
            btnStart.text = "Start"
        }

        // Daftarin broadcast
        registerReceiver(pointReceiver, IntentFilter("NEW_CLICK_POINT"), RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(pointReceiver)
    }
}