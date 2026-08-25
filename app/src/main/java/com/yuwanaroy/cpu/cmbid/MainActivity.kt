package com.yuwanaroy.cpu.cmbid

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuwanaroy.cpu.cmbid.databinding.ActivityMainBinding
import com.yuwanaroy.cpu.cmbid.engine.ClickEngine
import com.yuwanaroy.cpu.cmbid.model.ClickPoint
import com.yuwanaroy.cpu.cmbid.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ItemAdapter
    private val clickList = mutableListOf<ClickPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        adapter = ItemAdapter(clickList) { position ->
            clickList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        binding.rvClickList.layoutManager = LinearLayoutManager(this)
        binding.rvClickList.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnEnableService.setOnClickListener {
            // Simpan state ke prefs
            PreferenceManager.setServiceEnabled(this, true)
            // Arahkan ke setting Accessibility
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        binding.btnStart.setOnClickListener {
            // Contoh: jalanin klik di titik pertama
            if (clickList.isNotEmpty()) {
                val point = clickList[0]
                ClickEngine.startAutoClick(point.x, point.y, point.delay)
            }
        }

        binding.btnStop.setOnClickListener {
            ClickEngine.stopAutoClick()
        }

        binding.btnAddPoint.setOnClickListener {
            // Contoh nambah titik dummy. Nanti bisa diganti pake overlay buat pilih titik
            clickList.add(ClickPoint(x = 500f, y = 1000f, delay = 1000L))
            adapter.notifyItemInserted(clickList.size - 1)
        }
    }
}