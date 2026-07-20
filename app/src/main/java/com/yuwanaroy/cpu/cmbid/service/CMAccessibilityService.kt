package com.yuwanaroy.cpu.cmbid.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.yuwanaroy.cpu.cmbid.model.Point

class CMAccessibilityService : AccessibilityService() {

    companion object { var instance: CMAccessibilityService? = null }

    private val clickEngine = ClickEngine() // ✅ BALIK PAKE CLICKENGINE

    override fun onServiceConnected() { super.onServiceConnected(); instance = this }
    override fun onDestroy() { super.onDestroy(); clickEngine.stop(); instance = null }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() { clickEngine.stop() }

    fun startService(points: List<Point>) { // ✅ PANGGIL CLICKENGINE
        clickEngine.start(points)
    }

    fun stopService() { // ✅ PANGGIL CLICKENGINE
        clickEngine.stop()
    }

    // FUNGSI BARU: DI PANGGIL CLICKENGINE
    fun findAndClickValidPrice(settings: Point): Boolean {
        val root = rootInActiveWindow?: return false
        val nodes = root.findAccessibilityNodeInfosByText("Rp")

        for (node in nodes) {
            val text = node.text?.toString()?: continue
            val harga = text.replace(Regex("[^0-9]"), "").toIntOrNull()?: continue

            if (harga >= settings.minHarga && harga <= settings.maxHarga) {
                val parent = findClickableParent(node)
                if (parent!= null) {
                    performClick(parent)
                    return true // Kasih tau ClickEngine: "sudah klik"
                }
            }
        }
        return false // Kasih tau ClickEngine: "gak nemu harga cocok"
    }

    private fun findClickableParent(node: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        var current = node
        while (current!= null) {
            if (current.isClickable) return current
            if (current.text?.toString()?.contains("TERIMA", true) == true && current.isClickable) return current
            current = current.parent
        }
        return null
    }

    fun performClick(x: Int, y: Int) { // Dipake kalau mau klik koordinat
        val path = Path().apply { moveTo(x.toFloat(), y.toFloat()) }
        val gesture = GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 100)).build()
        dispatchGesture(gesture, null, null)
    }

    fun performClick(node: AccessibilityNodeInfo) { // Dipake buat klik node TERIMA
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        performClick(bounds.centerX(), bounds.centerY())
    }

    fun performScroll(jarak: Int) {
        val path = Path().apply {
            moveTo(500f, 1500f)
            lineTo(500f, 1500f - jarak)
        }
        val gesture = GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 300)).build()
        dispatchGesture(gesture, null, null)
    }
}
