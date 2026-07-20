package com.yuwanaroy.cpu.cmbid.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import kotlinx.coroutines.*

class CMAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private var isRunning = false
    private var scanJob: Job? = null

    private var clickDelay = 150L 
    private var interval = 300L 
    private var hargaMin = 10000
    private var hargaMax = 50000
    private var jarakMax = 1000

    companion object { private const val TAG = "CMBID_Service" }

    override fun onServiceConnected() {
        super.onServiceConnected()
        loadSettings()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // PAKE EVENT LAGI BIAR HEMAT BATERAI
        // Cuma scan pas ada perubahan, bukan tiap 300ms
        if (!isRunning || event == null) return
        
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || 
            event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            
            // Cancel job lama biar gak numpuk
            scanJob?.cancel()
            scanJob = serviceScope.launch {
                delay(100) // kasih jeda kecil biar UI selesai render
                findAndClickBidButton(rootInActiveWindow)
            }
        }
    }

    private suspend fun findAndClickBidButton(node: AccessibilityNodeInfo?) {
        if (node == null) return // SAFETY 1
        scanNode(node)
    }

    private suspend fun scanNode(node: AccessibilityNodeInfo) {
        val text = node.text?.toString()?.lowercase()
                
        if (text != null && (text.contains("bid") || text.contains("terima") || text.contains("ambil"))) {
            // FIX: KIRIM rootInActiveWindow YG BISA NULL
            if (checkHargaDanJarak(rootInActiveWindow)) { 
                performClick(node)
                delay(clickDelay)
            }
        }

        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { scanNode(it) }
        }
    }

    // FIX: TERIMA NULL
    private fun checkHargaDanJarak(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false // SAFETY 2

        var harga = 0
        var jarak = 0
        val allText = mutableListOf<String>()
        collectText(node, allText)
        
        for (text in allText) {
            if (text.contains("rp", true)) {
                harga = text.replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0
            }
            if ((text.contains("m") || text.contains("km")) && !text.contains("rp", true)) {
                jarak = text.replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0
            }
        }
        Log.d(TAG, "Cek Harga: $harga, Jarak: $jarak")
        return harga in hargaMin..hargaMax && jarak <= jarakMax
    }

    private fun collectText(node: AccessibilityNodeInfo, list: MutableList<String>) {
        node.text?.let { list.add(it.toString()) }
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { collectText(it, list) }
        }
    }

    private fun performClick(node: AccessibilityNodeInfo) {
        if (node.isClickable) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            Log.d(TAG, "CLICKED: ${node.text}")
        } else if (node.parent != null) {
            performClick(node.parent)
        }
    }

    fun performGestureClick(x: Float, y: Float) {
        val path = Path()
        path.moveTo(x, y)
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100)).build()
        dispatchGesture(gesture, null, null)
    }

    private fun loadSettings() {
        val prefs = getSharedPreferences("cmbid_prefs", Context.MODE_PRIVATE)
        interval = prefs.getInt("interval", 300).toLong()
        clickDelay = prefs.getInt("delay", 150).toLong()
        hargaMin = prefs.getInt("min", 10000)
        hargaMax = prefs.getInt("max", 50000)
        jarakMax = prefs.getInt("jarak", 1000)
        isRunning = true
    }

    override fun onInterrupt() { isRunning = false; scanJob?.cancel() }
    override fun onDestroy() { super.onDestroy(); serviceScope.cancel(); isRunning = false }
}
