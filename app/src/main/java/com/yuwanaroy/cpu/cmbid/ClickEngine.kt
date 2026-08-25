package com.yuwanaroy.cpu.cmbid.engine

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.yuwanaroy.cpu.cmbid.CMAccessibilityService

object ClickEngine {
    private lateinit var context: Context
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    fun init(ctx: Context) {
        context = ctx.applicationContext
    }

    fun startAutoClick(x: Float, y: Float, delay: Long = 1000L) {
        if (isRunning) return
        isRunning = true
        runClick(x, y, delay)
    }

    private fun runClick(x: Float, y: Float, delay: Long) {
        handler.postDelayed({
            CMAccessibilityService.instance?.performClick(x, y)
            if (isRunning) runClick(x, y, delay)
        }, delay)
    }

    fun stopAutoClick() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }
}