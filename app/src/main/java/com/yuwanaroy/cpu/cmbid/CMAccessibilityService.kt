package com.yuwanaroy.cpu.cmbid

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import com.yuwanaroy.cpu.cmbid.engine.ClickEngine
import com.yuwanaroy.cpu.cmbid.utils.PreferenceManager

class CMAccessibilityService : AccessibilityService() {

    companion object {
        var instance: CMAccessibilityService? = null
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        PreferenceManager.setServiceEnabled(this, true)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Disini nanti logika auto click nya jalan
        // Contoh: ClickEngine.startAutoClick()
    }

    override fun onInterrupt() {
        instance = null
        PreferenceManager.setServiceEnabled(this, false)
    }

    // Fungsi buat klik dari ClickEngine
    fun performClick(x: Float, y: Float) {
        val path = Path()
        path.moveTo(x, y)
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()
        dispatchGesture(gesture, null, null)
    }
}