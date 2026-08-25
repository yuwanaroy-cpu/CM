package com.yuwanaroy.cpu.cmbid.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuwanaroy.cpu.cmbid.model.ClickPoint

class PreferenceManager(context: Context) {
    private val prefs = context.getSharedPreferences("CM_PREFS", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveClickPoints(list: List<ClickPoint>) {
        val json = gson.toJson(list)
        prefs.edit().putString("CLICK_POINTS", json).apply()
    }

    fun getClickPoints(): MutableList<ClickPoint> {
        val json = prefs.getString("CLICK_POINTS", "[]")
        val type = object : TypeToken<MutableList<ClickPoint>>() {}.type
        return gson.fromJson(json, type)?: mutableListOf()
    }
}