package com.example.mpinspector.ui.adapters

/**
 * Interface for listening RecycleView item clicks.
 * @param TData Type of the RecycleView items.
 */
interface OnRecycleViewItemClick<TData> {
    fun onItemClick(itemData: TData)
}