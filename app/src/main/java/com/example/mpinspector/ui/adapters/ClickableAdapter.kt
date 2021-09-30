package com.example.mpinspector.ui.adapters

import androidx.databinding.ViewDataBinding

/**
 * RecycleViewAdapter that supports item clicks. Pass in the click
 * event listener to constructor and you are good to go.
 *
 * @param TData Type of the list items.
 * @param TBinding : ViewBinding List items' layout's ViewBinder.
 * @property listener OnRecycleViewItemClick On click listener.
 */
abstract class ClickableAdapter<TData, TBinding : ViewDataBinding>(
    items: List<TData>,
    itemLayout: Int,
    protected val listener: OnRecycleViewItemClick
) : GenericAdapter<TData, TBinding, ClickableViewHolder<TBinding>>(items, itemLayout) {

    override fun createViewHolder(binding: TBinding): ClickableViewHolder<TBinding> {
        return ClickableViewHolder(binding, listener)
    }
}