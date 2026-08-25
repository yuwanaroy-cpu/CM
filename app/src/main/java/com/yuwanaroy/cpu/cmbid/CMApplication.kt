package com.yuwanaroy.cpu.cmbid

import android.app.Application
import com.yuwanaroy.cpu.cmbid.engine.ClickEngine
import com.yuwanaroy.cpu.cmbid.utils.PreferenceManager

class CMApplication : Application() {

    companion object {
        lateinit var instance: CMApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Init engine sekali di awal
        ClickEngine.init(this)
    }
}