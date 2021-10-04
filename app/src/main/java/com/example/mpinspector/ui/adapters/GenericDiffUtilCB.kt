package com.example.mpinspector.ui.adapters

import androidx.recyclerview.widget.DiffUtil

class GenericDiffUtilCB<T>(private val old: List<T>, private val new: List<T>,
                           private val comp: (T, T) -> Boolean) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = old.size
    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return comp(old[oldItemPosition],  new[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return comp(old[oldItemPosition],  new[newItemPosition])
    }
}