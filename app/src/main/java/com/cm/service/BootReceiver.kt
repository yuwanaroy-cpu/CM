package com.yuwanaroy.cpu.cmbid.receiver
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yuwanaroy.cpu.cmbid.service.CMForegroundService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val i = Intent(context, CMForegroundService::class.java)
            i.action = CMForegroundService.ACTION_START
            context.startForegroundService(i)
        }
    }
}
