package com.cm

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView
    private lateinit var interval: EditText
    private lateinit var delay: EditText

    private lateinit var btnStart: Button
    private lateinit var btnAdd: Button

    private lateinit var switchAutobidDistance: Switch

    private lateinit var seekMinPrice: SeekBar
    private lateinit var seekMaxPrice: SeekBar
    private lateinit var seekDistance: SeekBar

    private lateinit var txtMinPrice: TextView
    private lateinit var txtMaxPrice: TextView
    private lateinit var txtDistance: TextView

    private lateinit var prefs: android.content.SharedPreferences

    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("cm_settings", Context.MODE_PRIVATE)

        status = findViewById(R.id.status)
        interval = findViewById(R.id.interval)
        delay = findViewById(R.id.delay)

        btnStart = findViewById(R.id.btnStart)
        btnAdd = findViewById(R.id.btnAdd)

        switchAutobidDistance = findViewById(R.id.switchAutobidDistance)

        seekMinPrice = findViewById(R.id.seekMinPrice)
        seekMaxPrice = findViewById(R.id.seekMaxPrice)
        seekDistance = findViewById(R.id.seekDistance)

        txtMinPrice = findViewById(R.id.txtMinPrice)
        txtMaxPrice = findViewById(R.id.txtMaxPrice)
        txtDistance = findViewById(R.id.txtDistance)

        switchAutobidDistance.isChecked =
            prefs.getBoolean("distance_enabled", false)

        seekMinPrice.progress =
            prefs.getInt("min_price", 0)

        seekMaxPrice.progress =
            prefs.getInt("max_price", 100000)

        seekDistance.progress =
            prefs.getInt("distance", 1000)

        updateLabel()

        switchAutobidDistance.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("distance_enabled", checked).apply()
        }

        seekMinPrice.setOnSeekBarChangeListener(listener)

        seekMaxPrice.setOnSeekBarChangeListener(listener)

        seekDistance.setOnSeekBarChangeListener(listener)

        btnStart.setOnClickListener {

            running = !running

            if (running) {
                status.text = "Status : RUNNING"
                btnStart.text = "STOP"
            } else {
                status.text = "Status : STOP"
                btnStart.text = "START"
            }
        }

        btnAdd.setOnClickListener {
            status.text = "Point berhasil ditambahkan"
        }
    }

    private val listener = object : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(
            seekBar: SeekBar?,
            progress: Int,
            fromUser: Boolean
        ) {

            updateLabel()

            prefs.edit()
                .putInt("min_price", seekMinPrice.progress)
                .putInt("max_price", seekMaxPrice.progress)
                .putInt("distance", seekDistance.progress)
                .apply()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    private fun updateLabel() {

        txtMinPrice.text =
            "Harga Minimal : ${seekMinPrice.progress}"

        txtMaxPrice.text =
            "Harga Maksimal : ${seekMaxPrice.progress}"

        txtDistance.text =
            "Jarak : ${seekDistance.progress} m"
    }
}
