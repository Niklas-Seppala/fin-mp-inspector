package com.example.mpinspector.ui

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mpinspector.R
import com.example.mpinspector.utils.PartyMapper

object Binders {
    @JvmStatic
    @BindingAdapter("setPartyIcon")
    fun setImage(view: ImageView, party: String?) {
        if (party == null) {
            Log.e("Binding", "Party is NULL")
            return
        }
        view.setImageResource(PartyMapper.partyIcon(party))
    }

    @JvmStatic
    @BindingAdapter("setImageRes")
    fun setImageRes(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("setMinisterStatus")
    fun setMinister(view: TextView, isMinister: Boolean) {
        view.text = if (isMinister) "Minister" else ""
    }

    @JvmStatic
    @BindingAdapter("setProfileImage")
    fun setProfileImage(view: ImageView, image: Bitmap?) {
        if (image == null) {
            Log.e("Binding", "image is NULL")
            return
        }
        view.setImageBitmap(image)
    }


    @JvmStatic
    @BindingAdapter("setMinisterIcon")
    fun setMinisterIcon(view: ImageView, isMinister: Boolean) {
        if (isMinister) {
            view.setImageResource(R.drawable.ic_minister)
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
            view.setImageDrawable(null)
        }
    }


}