package com.yuwanaroy.cpu.cmbid.engine

import com.yuwanaroy.cpu.cmbid.model.Point

class ClickEngine {

    private val points = mutableListOf<Point>()

    fun addPoint(point: Point) {
        points.add(point)
    }

    fun clearPoints() {
        points.clear()
    }

    fun getPoints(): List<Point> {
        return points
    }
}
