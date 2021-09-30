package com.example.mpinspector.ui.adapters

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class ViewHolder<TBinding : ViewDataBinding>(val binding: TBinding) : RecyclerView.ViewHolder(binding.root)

//class ViewHolder<B : ViewDataBinding>(
//    val binding: B,
//    private val listener: OnRecycleViewItemClick? = null
//) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
//    init {
//        itemView.setOnClickListener(this)
//    }
//
//    override fun onClick(p0: View?) {
//        listener?.onItemClick(absoluteAdapterPosition)
//    }
//}