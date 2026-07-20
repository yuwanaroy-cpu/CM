package com.yuwanaroy.cpu.cmbid.utils

import android.content.Context

class PreferenceManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("cm_settings", Context.MODE_PRIVATE)

    var interval: Int
        get() = prefs.getInt("interval", 100)
        set(value) = prefs.edit().putInt("interval", value).apply()

    var delay: Int
        get() = prefs.getInt("delay", 0)
        set(value) = prefs.edit().putInt("delay", value).apply()

    var distanceEnabled: Boolean
        get() = prefs.getBoolean("distance_enabled", false)
        set(value) = prefs.edit().putBoolean("distance_enabled", value).apply()

    var minPrice: Int
        get() = prefs.getInt("min_price", 0)
        set(value) = prefs.edit().putInt("min_price", value).apply()

    var maxPrice: Int
        get() = prefs.getInt("max_price", 100000)
        set(value) = prefs.edit().putInt("max_price", value).apply()

    var distance: Int
        get() = prefs.getInt("distance", 1000)
        set(value) = prefs.edit().putInt("distance", value).apply()
}
