package com.example.mpinspector.ui.adapters

import android.view.View
import androidx.databinding.ViewDataBinding

class ClickableViewHolder<B : ViewDataBinding>(
    binding: B,
    private val listener: OnRecycleViewItemClick? = null
) : ViewHolder<B>(binding), View.OnClickListener {
    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        listener?.onItemClick(absoluteAdapterPosition)
    }
}