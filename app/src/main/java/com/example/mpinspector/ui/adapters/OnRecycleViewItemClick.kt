package com.example.mpinspector.ui.adapters

/**
 * Interface for listening RecycleView item clicks.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 *
 * @param TData Type of the RecycleView items.
 */
interface OnRecycleViewItemClick<TData> {
    fun onItemClick(itemData: TData)
}