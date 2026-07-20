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

        val settings = pointList[currentIndex]

        // 1. Coba klik dulu. Balik true kalau berhasil klik
        val clicked = CMAccessibilityService.instance?.findAndClickValidPrice(settings)?: false

        // 2. Kalau berhasil klik -> tunggu "delay". Kalau gagal -> langsung lanjut
        val nextDelay = if (clicked) settings.delay else 0L

        handler.postDelayed({
            // 3. Pindah ke setting berikutnya
            currentIndex = (currentIndex + 1) % pointList.size

            // 4. Scroll kalau ada
            if (settings.jarak > 0) {
                CMAccessibilityService.instance?.performScroll(settings.jarak)
            }

            // 5. Loop lagi dengan interval
            handler.postDelayed({ runNextClick() }, settings.interval)

        }, nextDelay)
    }
}
