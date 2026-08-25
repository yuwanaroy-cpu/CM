package com.yuwanaroy.cpu.cmbid.engine

import android.content.Context
import com.yuwanaroy.cpu.cmbid.model.ClickPoint
import com.yuwanaroy.cpu.cmbid.service.CMAccessibilityService

object ClickEngine {
    private var context: Context? = null

    fun init(ctx: Context) {
        context = ctx.applicationContext
    }

    fun startAutoClick(points: List<ClickPoint>) {
        CMAccessibilityService.instance?.doAutoClick(points)
    }

    fun stopAutoClick() {
        // Buat simpel kita stop di MainActivity aja dengan flag
    }
}