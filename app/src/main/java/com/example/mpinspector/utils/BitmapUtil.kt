package com.example.mpinspector.utils

import android.graphics.*

object MyTime {
    val timestampLong: Long
        get() = System.currentTimeMillis()

    val timestampInt: Int
        get() = (System.currentTimeMillis() / 1000L).toInt()
}


object BitmapUtil {

    fun resizeBitmap(bitmap: Bitmap, destWidth: Int) : Bitmap {
        val scale = destWidth.toDouble() / bitmap.width
        val destHeight = (scale * bitmap.height).toInt()
        return Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
    }

    fun roundCorners(bm: Bitmap, px: Int = 30): Bitmap {
        val out = Bitmap.createBitmap(bm.width, bm.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)

        val paint = Paint()
        val rect = Rect(0, 0, bm.width, bm.height)
        val rectF = RectF(rect)
        val roundPx = px.toFloat()

        paint.isAntiAlias = true
        canvas.drawARGB(0,0,0,0)
        paint.color = 0xff000000.toInt()
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bm, rect, rect, paint)

        return out
    }
}