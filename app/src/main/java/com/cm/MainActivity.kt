package com.cm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView
    private lateinit var interval: EditText
    private lateinit var delay: EditText
    private lateinit var btnStart: Button
    private lateinit var btnAdd: Button

    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)
        interval = findViewById(R.id.interval)
        delay = findViewById(R.id.delay)
        btnStart = findViewById(R.id.btnStart)
        btnAdd = findViewById(R.id.btnAdd)

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
}
