package com.example.mpinspector.ui.adapters

/**
 * Interface for listening RecycleView item clicks.
 * @param T Type of the RecycleView items.
 */
interface OnRecycleViewItemClick<T> {
    fun onItemClick(itemData: T)
}