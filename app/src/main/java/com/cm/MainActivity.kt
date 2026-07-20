package com.cm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cm.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var pref: PreferenceManager

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

    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pref = PreferenceManager(this)

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

        // Muat pengaturan yang tersimpan
        switchAutobidDistance.isChecked = pref.distanceEnabled
        seekMinPrice.progress = pref.minPrice
        seekMaxPrice.progress = pref.maxPrice
        seekDistance.progress = pref.distance

        updateLabel()

        switchAutobidDistance.setOnCheckedChangeListener { _, checked ->
            pref.distanceEnabled = checked
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

            pref.minPrice = seekMinPrice.progress
            pref.maxPrice = seekMaxPrice.progress
            pref.distance = seekDistance.progress
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    private fun updateLabel() {

        txtMinPrice.text = "Harga Minimal : ${seekMinPrice.progress}"

        txtMaxPrice.text = "Harga Maksimal : ${seekMaxPrice.progress}"

        txtDistance.text = "Jarak : ${seekDistance.progress} m"
    }
}
