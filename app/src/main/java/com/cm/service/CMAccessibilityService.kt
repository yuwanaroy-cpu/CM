package com.cm.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import com.cm.model.Point

class CMAccessibilityService : AccessibilityService() {


    companion object {

        var instance: CMAccessibilityService? = null

    }


    private val handler =
        Handler(Looper.getMainLooper())


    private var pointList: List<Point> =
        emptyList()


    private var currentIndex = 0


    private var isRunning = false



    override fun onServiceConnected() {

        super.onServiceConnected()

        instance = this

    }




    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }




    override fun onInterrupt() {

        stopService()

    }





    override fun onDestroy() {

        super.onDestroy()

        stopService()

        instance = null

    }





    fun startService(points: List<Point>) {


        if (points.isEmpty()) return


        pointList = points


        currentIndex = 0


        isRunning = true


        runAutoClick()

    }






    fun stopService() {


        isRunning = false


        handler.removeCallbacksAndMessages(null)

    }







    private fun runAutoClick() {


        if (!isRunning) return


        if (pointList.isEmpty()) return



        val point =
            pointList[currentIndex]



        performClick(
            point.x,
            point.y
        )



        currentIndex =
            (currentIndex + 1) % pointList.size




        handler.postDelayed(

            {

                runAutoClick()

            },

            point.interval.toLong()

        )

    }







    private fun performClick(
        x: Int,
        y: Int
    ) {


        val path = Path()


        path.moveTo(
            x.toFloat(),
            y.toFloat()
        )



        val gesture =
            GestureDescription.Builder()


                .addStroke(

                    GestureDescription.StrokeDescription(

                        path,

                        0,

                        100

                    )

                )


                .build()



        dispatchGesture(

            gesture,

            null,

            null

        )

    }

}
