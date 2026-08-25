package com.yuwanaroy.cpu.cmbid.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import com.yuwanaroy.cpu.cmbid.engine.ClickEngine

class CMAccessibilityService : AccessibilityService() {

    companion object {
        var instance: CMAccessibilityService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    // Fungsi inti buat klik
    fun performClick(x: Float, y: Float) {
        val path = Path()
        path.moveTo(x, y)
        
        val gestureBuilder = GestureDescription.Builder()
        val strokeDescription = GestureDescription.StrokeDescription(path, 0, 50) // 50ms = durasi tap
        gestureBuilder.addStroke(strokeDescription)
        
        dispatchGesture(gestureBuilder.build(), null, null)
    }

    // Dipanggil dari ClickEngine
    fun doAutoClick(points: List<com.yuwanaroy.cpu.cmbid.model.ClickPoint>) {
        val handler = Handler(Looper.getMainLooper())
        var totalDelay = 0L

        for (point in points) {
            handler.postDelayed({
                performClick(point.x, point.y)
            }, totalDelay)
            totalDelay += point.delay
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
}