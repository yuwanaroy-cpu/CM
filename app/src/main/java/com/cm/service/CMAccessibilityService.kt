package com.cm.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class CMAccessibilityService : AccessibilityService() {

    companion object {
        var instance: CMAccessibilityService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Akan kita gunakan nanti
    }

    override fun onInterrupt() {
        // Akan kita gunakan nanti
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
