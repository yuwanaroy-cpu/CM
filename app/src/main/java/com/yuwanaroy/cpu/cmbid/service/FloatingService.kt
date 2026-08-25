package com.yuwanaroy.cpu.cmbid.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast

class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private var params: WindowManager.LayoutParams? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        floatingView = LayoutInflater.from(this).inflate(android.R.layout.simple_button, null)
        val btn = Button(this).apply { text = "Titik" }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params!!.gravity = Gravity.TOP or Gravity.START
        params!!.x = 100
        params!!.y = 100

        windowManager.addView(btn, params)

        // Geser tombol
        var lastX = 0
        var lastY = 0
        var firstX = 0
        var firstY = 0

        btn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    firstX = event.rawX.toInt()
                    firstY = event.rawY.toInt()
                    lastX = params!!.x
                    lastY = params!!.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params!!.x = lastX + (event.rawX.toInt() - firstX)
                    params!!.y = lastY + (event.rawY.toInt() - firstY)
                    windowManager.updateViewLayout(btn, params)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Kalau cuma tap, bukan geser
                    if (kotlin.math.abs(event.rawX.toInt() - firstX) < 10 && kotlin.math.abs(event.rawY.toInt() - firstY) < 10) {
                        val x = event.rawX
                        val y = event.rawY
                        Toast.makeText(this, "Titik: X=$x, Y=$y", Toast.LENGTH_SHORT).show()
                        // Kirim ke MainActivity
                        val intent = Intent("NEW_CLICK_POINT")
                        intent.putExtra("x", x)
                        intent.putExtra("y", y)
                        sendBroadcast(intent)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // windowManager.removeView(floatingView) // kalau pake layout
    }

    override fun onBind(intent: Intent?): IBinder? = null
}