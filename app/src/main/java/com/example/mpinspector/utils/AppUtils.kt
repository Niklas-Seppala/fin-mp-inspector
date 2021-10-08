package com.example.mpinspector.utils

import android.content.Context
import android.graphics.*
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentCommentBinding
import com.example.mpinspector.databinding.FragmentListMpItemBinding
import java.util.*

typealias CommentBinding = FragmentCommentBinding
typealias MpItemBinding = FragmentListMpItemBinding

object MyTime {
    val timestampLong: Long
        get() = System.currentTimeMillis()

    val timestampInt: Int
        get() = (System.currentTimeMillis() / 1000L).toInt()
}

object Toaster {
    /**
     * Since custom toast views are deprecated, this evil,
     * ugly hack manages to set the font color.
     * @param context Context?
     * @param text String
     */
    fun make(context: Context?, text: String) {
        context?.let {
            val color = it.resources.getColor(R.color.purple_700)
            Toast.makeText(context,
                HtmlCompat.fromHtml("<font color='$color'>$text</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                Toast.LENGTH_SHORT)
                .show()
        }
    }
}

object Year {
    val current = Calendar.getInstance().get(Calendar.YEAR)
}

object BitmapUtil {

    fun resizeBitmap(bitmap: Bitmap, destWidth: Int): Bitmap {
        val scale = destWidth.toDouble() / bitmap.width
        val destHeight = (scale * bitmap.height).toInt()
        return Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
    }


    fun circleCrop(bitmap: Bitmap, x: Int, y: Int, diameter: Int): Bitmap {

        val rect = Rect(0, 0, diameter, diameter)

        val out = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)

        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = 0xff000000.toInt()
        canvas.drawCircle(x.toFloat(), y.toFloat(), diameter.toFloat() / 2, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return out
    }



    fun roundCorners(bm: Bitmap, px: Int = 30): Bitmap {
        val out = Bitmap.createBitmap(bm.width, bm.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)

        val paint = Paint()
        val rect = Rect(0, 0, bm.width, bm.height)
        val rectF = RectF(rect)
        val roundPx = px.toFloat()

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = 0xff000000.toInt()
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bm, rect, rect, paint)

        return out
    }
}

class NoSuchPartyException(partyId: String, msg: String = "No such \"$partyId\" party mapped.") :
    Exception(msg)

object PartyMapper {

    data class Party(val name: Int, val icon: Int)

    private val partyMap = mapOf(
        "kok" to Party(R.string.partyKok, R.mipmap.ic_party_kok),
        "vas" to Party(R.string.partyVas, R.mipmap.ic_party_vas),
        "vihr" to Party(R.string.partyVihr, R.mipmap.ic_party_vihr),
        "ps" to Party(R.string.partyPs, R.mipmap.ic_party_ps),
        "sd" to Party(R.string.partySd, R.mipmap.ic_party_sd),
        "liik" to Party(R.string.partyLiik, R.mipmap.ic_party_liik),
        "r" to Party(R.string.partyR, R.mipmap.ic_party_r),
        "kd" to Party(R.string.partyKd, R.mipmap.ic_party_kd),
        "kesk" to Party(R.string.partyKesk, R.mipmap.ic_party_kesk)
    )

    @Throws(NoSuchPartyException::class)
    fun partyName(id: String) = partyMap[id]?.name ?: throw NoSuchPartyException(id)

    @Throws(NoSuchPartyException::class)
    fun partyIcon(id: String) = partyMap[id]?.icon ?: throw NoSuchPartyException(id)
}