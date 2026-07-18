package com.cm.utils

import android.content.Context

class PreferenceManager(context: Context) {

    private val prefs = context.getSharedPreferences("cm_settings", Context.MODE_PRIVATE)

    fun setInterval(value: Int) {
        prefs.edit().putInt("interval", value).apply()
    }

    fun getInterval(): Int {
        return prefs.getInt("interval", 100)
    }

    fun setDelay(value: Int) {
        prefs.edit().putInt("delay", value).apply()
    }

    fun getDelay(): Int {
        return prefs.getInt("delay", 0)
    }

    fun setRepeat(value: Int) {
        prefs.edit().putInt("repeat", value).apply()
    }

    fun getRepeat(): Int {
        return prefs.getInt("repeat", -1)
    }
}
