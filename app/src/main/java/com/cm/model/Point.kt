package com.cm.model

data class Point(
    val x: Int,          // posisi klik X
    val y: Int,          // posisi klik Y
    val interval: Int,   // interval autobid
    val delay: Int,      // jeda klik
    val minHarga: Int,   // harga minimal
    val maxHarga: Int,   // harga maksimal
    val jarak: Int       // filter jarak meter
)
