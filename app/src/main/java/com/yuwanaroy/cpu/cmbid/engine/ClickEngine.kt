package com.yuwanaroy.cpu.cmbid.service

import android.os.Handler
import android.os.Looper
import com.yuwanaroy.cpu.cmbid.model.Point

class ClickEngine {

    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var pointList: List<Point> = emptyList()
    private var currentIndex = 0

    fun start(points: List<Point>) {
        if (points.isEmpty()) return
        pointList = points
        isRunning = true
        currentIndex = 0
        runNextClick()
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun runNextClick() {
        if (!isRunning) return

        val point = pointList[currentIndex]

        // Delay awal
        handler.postDelayed({
            // KLIK
            CMAccessibilityService.instance?.performClick(point.x, point.y)
            
            // Scroll setelah klik kalau jarak > 0
            if (point.jarak > 0) {
                CMAccessibilityService.instance?.performScroll(point.jarak)
            }

            // Pindah ke point berikutnya
            currentIndex = (currentIndex + 1) % pointList.size
            
            // Loop dengan interval
            handler.postDelayed({ runNextClick() }, point.interval)

        }, point.delay)
    }
}
