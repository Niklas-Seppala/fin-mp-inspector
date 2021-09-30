package com.example.mpinspector.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class ClickableViewHolder(binding: ViewBinding, val listener: GenericAdapter.OnMyItemClick) : ViewHolder(binding), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        listener.onItemClick(absoluteAdapterPosition)
    }
}

open class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)