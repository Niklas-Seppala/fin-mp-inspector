package com.example.mpinspector.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.mpinspector.MyApp
import com.example.mpinspector.R

abstract class GenericAdapter<TData, TBinding : ViewDataBinding, THolder: ViewHolder<TBinding>>(
    protected val items: List<TData>,
    protected val itemLayout: Int)
    : RecyclerView.Adapter<THolder>() {

    protected abstract fun createViewHolder(binding: TBinding): THolder
//
//    protected open fun createViewHolder(binding: TBinding): THolder {
//        return ViewHolder(binding)
//    }

    private val _currentItems = items.toMutableList()
    val currentItems: List<TData>
        get() = _currentItems

    override fun getItemCount(): Int = _currentItems.size

    override fun onBindViewHolder(holder: THolder, position: Int) {
        holder.binding.root.setBackgroundColor(
            if (position % 2 == 0 ) darkColor
            else lightColor
        )
        bind(holder.binding, _currentItems[position])
    }

    protected abstract fun bind(binding: TBinding, item: TData)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): THolder {
        return createViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            itemLayout, parent, false
        ))
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun updateItems(new: List<TData>) {
        _currentItems.clear()
        _currentItems.addAll(new)
        notifyDataSetChanged()
    }

    protected companion object {
        var lightColor: Int = ContextCompat.getColor(MyApp.appContext, R.color.comment_dark)
        var darkColor: Int = ContextCompat.getColor(MyApp.appContext, R.color.comment_light)
    }
}