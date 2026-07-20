package com.yuwanaroy.cpu.cmbid.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.yuwanaroy.cpu.cmbid.model.Point

class CMAccessibilityService : AccessibilityService() {

    companion object { var instance: CMAccessibilityService? = null }

    private val handler = Handler(Looper.getMainLooper())
    private var pointList: List<Point> = emptyList()
    private var isRunning = false
    private var runnable: Runnable? = null

    override fun onServiceConnected() { super.onServiceConnected(); instance = this }
    override fun onDestroy() { super.onDestroy(); stopService(); instance = null }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() { stopService() }

    fun startService(points: List<Point>) {
        if (points.isEmpty()) return
        pointList = points
        isRunning = true
        runAutoClick()
    }

    fun stopService() {
        isRunning = false
        runnable?.let { handler.removeCallbacks(it) }
    }

    private fun runAutoClick() {
        if (!isRunning) return
        
        checkScreenAndClick()
        
        runnable = Runnable { runAutoClick() }
        handler.postDelayed(runnable!!, pointList.firstOrNull()?.interval?.toLong() ?: 2000)
    }

    private fun checkScreenAndClick() {
        val root = rootInActiveWindow ?: return
        val settings = pointList.firstOrNull() ?: return // pakai setting pertama

        // 1. Cari semua node yang ada text "Rp"
        val nodes = root.findAccessibilityNodeInfosByText("Rp")
        
        for (node in nodes) {
            val text = node.text?.toString() ?: continue
            
            // 2. Ambil angka dari "Rp10.000" -> 10000
            val harga = text.replace(Regex("[^0-9]"), "").toIntOrNull() ?: continue
            
            // 3. Cek apakah harga masuk range
            if (harga >= settings.minHarga && harga <= settings.maxHarga) {
                // 4. Cari tombol "TERIMA" di sekitar harga itu
                val parent = findClickableParent(node)
                if (parent != null) {
                    performClick(parent)
                    // delay setelah klik
                    handler.postDelayed({ }, settings.delay.toLong())
                    return
                }
            }
        }
    }

    private fun findClickableParent(node: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        var current = node
        while (current != null) {
            if (current.isClickable) return current
            if (current.text?.toString()?.contains("TERIMA", true) == true) return current
            current = current.parent
        }
        return null
    }

    private fun performClick(node: AccessibilityNodeInfo) {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        val path = Path().apply { moveTo(bounds.centerX().toFloat(), bounds.centerY().toFloat()) }
        val gesture = GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 100)).build()
        dispatchGesture(gesture, null, null)
    }
}
