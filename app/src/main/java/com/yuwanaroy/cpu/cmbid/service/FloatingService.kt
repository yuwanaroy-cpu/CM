package com.yuwanaroy.cpu.cmbid.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.Button
import com.yuwanaroy.cpu.cmbid.R

class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var params: WindowManager.LayoutParams

    override fun onCreate() {
        super.onCreate()
        
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatingView = LayoutInflater.from(this).inflate(R.layout_floating_widget, null)

        // Setting buat tombol melayang
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Wajib Android 8+
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 50
        params.y = 100

        windowManager.addView(floatingView, params)
        setupDrag()
        
        // Klik tombol STOP
        floatingView.findViewById<Button>(R.id.btnStopFloating).setOnClickListener {
            sendBroadcast(Intent("ACTION_STOP_CM_BID")) // kirim ke MainActivity
            stopSelf()
        }
    }

    // Biar tombol bisa digeser
    private fun setupDrag() {
        var lastX = 0
        var lastY = 0
        
        floatingView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX.toInt() - lastX
                    val dy = event.rawY.toInt() - lastY
                    params.x += dx
                    params.y += dy
                    windowManager.updateViewLayout(floatingView, params)
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                }
            }
            true
        }
    }

    override fun onDestroy() { 
        super.onDestroy() 
        if(::floatingView.isInitialized) windowManager.removeView(floatingView) 
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
