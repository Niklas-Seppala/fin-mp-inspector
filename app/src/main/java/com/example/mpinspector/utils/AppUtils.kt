package com.example.mpinspector.utils

import android.content.Context
import android.graphics.*
import android.icu.text.DateFormat
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.example.mpinspector.R
import java.util.*

/**
 * Helper object for time related stuff.
 */
object MyTime {
    /**
     * Get current time as unix timestamp (MS)
     */
    val timestampLong: Long
        get() = System.currentTimeMillis()

    private const val format = "${DateFormat.ABBR_MONTH_DAY} ${DateFormat.HOUR24_MINUTE} ${DateFormat.YEAR}"

    // This year.
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    /**
     * Get datetime from timestamp using apps datetime format.
     * @param timestamp: Millis
     * @return String Formatted datetime string.
     */
    fun strTime(timestamp: Long): String {
        return DateFormat.getPatternInstance(format).format(Date(timestamp))
    }
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
            val color = it.resources.getColor(R.color.purple_700, null)
            Toast.makeText(context,
                HtmlCompat.fromHtml("<font color='$color'>$text</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                Toast.LENGTH_SHORT)
                .show()
        }
    }
}

/**
 * Bitmap manipulation utility functions.
 */
object BitmapUtil {

    /**
     * Resizes specified bitmap. Retains w/h ratio.
     * @param bitmap Bitmap Src Bitmap
     * @param destWidth Int New width.
     * @return Bitmap Resized Bitmap.
     */
    fun resizeBitmap(bitmap: Bitmap, destWidth: Int): Bitmap {
        val scale = destWidth.toDouble() / bitmap.width
        val destHeight = (scale * bitmap.height).toInt()
        return Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
    }

    /**
     * Rounds Bitmap corners based on provided bitmap size and round value.
     * @param bm Bitmap Src bitmap.
     * @param roundVal Int px.
     * @return Bitmap Bitmap with round corners.
     */
    fun roundCorners(bm: Bitmap, roundVal: Int = 30): Bitmap {
        val out = Bitmap.createBitmap(bm.width, bm.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)

        val paint = Paint()
        val rect = Rect(0, 0, bm.width, bm.height)
        val rectF = RectF(rect)
        val roundPx = roundVal.toFloat()

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

object IconService {
    /**
     * Get Like/Dislike icon from like boolean.
     * @param like Boolean like.
     * @return Int icon res id.
     */
    fun getLikeIconRes(like: Boolean) =
        if (like) R.drawable.ic_like
        else R.drawable.ic_dislike
}

/**
 * Helper object to map parties to icons and names
 */
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

    /**
     * Get party name from party id
     * @param id String party id.
     * @return Party name.
     */
    @Throws(NoSuchPartyException::class)
    fun partyName(id: String) = partyMap[id]?.name ?: throw NoSuchPartyException(id)

    /**
     * Get party icon res from party id.
     * @param id String party id.
     * @return Int party icon res id.
     */
    @Throws(NoSuchPartyException::class)
    fun partyIcon(id: String) = partyMap[id]?.icon ?: throw NoSuchPartyException(id)
}