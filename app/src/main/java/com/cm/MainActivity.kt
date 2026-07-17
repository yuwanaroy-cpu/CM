package com.cm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var txtTime: TextView
    private lateinit var btnSave: Button

    private val handler = Handler(Looper.getMainLooper())

    private val clockRunnable = object : Runnable {
        override fun run() {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            txtTime.text = sdf.format(Date())
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtTime = findViewById(R.id.time)
        btnSave = findViewById(R.id.save)

        btnSave.setOnClickListener {
            Toast.makeText(this, "Settings disimpan", Toast.LENGTH_SHORT).show()
        }

        handler.post(clockRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(clockRunnable)
    }
}
