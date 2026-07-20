package com.yuwanaroy.cpu.cmbid

import android.app.Application
import android.util.Log

class CMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("CMBID", "Aplikasi CM BID dimulai")
    }
}
