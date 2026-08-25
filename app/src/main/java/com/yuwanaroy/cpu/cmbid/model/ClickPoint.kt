package com.yuwanaroy.cpu.cmbid.model

data class ClickPoint(
    val x: Float,
    val y: Float,
    var delay: Long = 1000L // Ubah 'val' menjadi 'var' di sini
)
