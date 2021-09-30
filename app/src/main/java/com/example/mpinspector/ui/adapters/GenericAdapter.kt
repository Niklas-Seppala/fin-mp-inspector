package com.example.mpinspector.ui.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.mpinspector.MyApp
import com.example.mpinspector.R

abstract class GenericAdapter<T, B : ViewBinding>(protected val items: List<T>)
    : RecyclerView.Adapter<ViewHolder>() {

    protected val currentItems = items.toMutableList()

    protected abstract fun createBinding(parent: ViewGroup): B

    override fun getItemCount(): Int = currentItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.setBackgroundColor(
            if (position % 2 == 0 ) darkColor
            else lightColor
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(createBinding(parent))
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun updateItems(new: List<T>) {
        currentItems.clear()
        currentItems.addAll(new)
        notifyDataSetChanged()
    }

    protected companion object {
        var lightColor: Int = ContextCompat.getColor(MyApp.appContext, R.color.comment_dark)
        var darkColor: Int = ContextCompat.getColor(MyApp.appContext, R.color.comment_light)
    }

    protected open fun <N: NavDirections>navigateTo(nav: NavController, action: N, setArg: (N) -> Unit) {
        setArg(action)
        nav.navigate(action)
    }
}