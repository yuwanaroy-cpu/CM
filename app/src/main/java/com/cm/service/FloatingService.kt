package com.cm.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class FloatingService : Service() {


    override fun onCreate() {
        super.onCreate()

        Toast.makeText(
            this,
            "Floating CM aktif",
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
