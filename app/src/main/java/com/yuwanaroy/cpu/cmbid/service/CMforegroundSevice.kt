package com.yuwanaroy.cpu.cmbid.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.yuwanaroy.cpu.cmbid.MainActivity
import com.yuwanaroy.cpu.cmbid.R

class CMForegroundService : Service() {
    companion object {
        const val CHANNEL_ID = "cm_bid_channel"
        const val NOTIF_ID = 1
        const val ACTION_START = "START_FOREGROUND"
        const val ACTION_STOP = "STOP_FOREGROUND"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startForeground(NOTIF_ID, buildNotification("CM BID Aktif"))
            ACTION_STOP -> { stopForeground(true); stopSelf() }
        }
        return START_STICKY
    }

    private fun buildNotification(text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "CM BID Service", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        val pi = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("CM BID").setContentText(text).setSmallIcon(R.drawable.cm_icon).setContentIntent(pi).setOngoing(true).build()
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
