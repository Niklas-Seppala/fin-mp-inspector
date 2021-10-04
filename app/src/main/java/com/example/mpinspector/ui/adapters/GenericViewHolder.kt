package com.example.mpinspector.ui.adapters

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * ViewHolder class for GenericAdapter. Delegates possible click events
 * to Adapters OnRecycleViewItemClick listener, if it is provided.
 *
 * @param TData List item Type.
 * @param TBinding : ViewDataBinding Type of the item layout ViewDataBiding.
 * @property binding TBinding ViewDataBinding object.
 * @property items List<TData> Reference to Adapter Items
 * @property listener OnRecycleViewItemClick<TData>? Click event listener.
 */
class GenericViewHolder<TData, TBinding : ViewDataBinding>(
    val binding: TBinding,
    private val items: List<TData>,
    private val listener: OnRecycleViewItemClick<TData>? = null
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    fun itemAtCurrentPos(): TData = items[absoluteAdapterPosition]

    init {
        itemView.setOnClickListener(this)
    }

    /**
     * View.OnClickListener event delegated to possible
     * OnRecycleViewItemClick listener.
     * @param view View Clicked view.
     */
    override fun onClick(view: View?) {
        listener?.onItemClick(itemAtCurrentPos())
    }
}