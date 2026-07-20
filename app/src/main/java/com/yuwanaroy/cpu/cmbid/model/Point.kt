package com.yuwanaroy.cpu.cmbid.model

data class Point(
    var x: Int,
    var y: Int,
    val interval: Long,
    val delay: Long,
    val minHarga: Int,
    val maxHarga: Int,
    val jarak: Int
)
