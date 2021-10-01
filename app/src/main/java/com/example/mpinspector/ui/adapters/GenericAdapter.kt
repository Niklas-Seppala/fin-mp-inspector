package com.example.mpinspector.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.mpinspector.MyApp
import com.example.mpinspector.R

/**
 * Jack of all trades RecycleViewAdapter base class with generic data and binding.
 * Provide list item type and ViewDataBinding type with related item layout resId.
 * RecycleView item clicks will be delegated to optional OnRecycleViewItemClick
 * listener object.
 *
 * @param TData List item Type.
 * @param TBinding : ViewDataBinding Type of the item layout ViewDataBiding.
 * @property items List<TData> All start items.
 * @property itemLayoutRes Int Item layout resId.
 * @property onItemClick OnRecycleViewItemClick<TData>? optional click listener.
 * @property currentItems List<TData> Currently displayed items.
 */
abstract class GenericAdapter<TData, TBinding : ViewDataBinding>(
    protected val items: List<TData>,
    protected val itemLayoutRes: Int,
    private val onItemClick: OnRecycleViewItemClick<TData>? = null
) : RecyclerView.Adapter<GenericViewHolder<TData, TBinding>>() {

    private val _currentItems = items.toMutableList()
    val currentItems: List<TData>
        get() = _currentItems

    override fun getItemCount(): Int = _currentItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<TData, TBinding> {
        return GenericViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                itemLayoutRes, parent, false
            ), _currentItems, onItemClick
        )
    }

    override fun onBindViewHolder(holder: GenericViewHolder<TData, TBinding>, position: Int) {
        // Set every other item background dark/light
        holder.binding.root.setBackgroundColor(
            if (position % 2 == 0) darkColor
            else lightColor
        )
        // Abstract func call
        hookUpItemWithView(holder.binding, _currentItems[position])
    }

    /**
     * Wire the item data to view any way you like it.
     * @param binding TBinding Binding object that holds layouts views.
     * @param item TData Item to operate.
     */
    protected abstract fun hookUpItemWithView(binding: TBinding, item: TData)

    /**
     * Replace current items with new items.
     * NOTE: notifyDataSetChanged() invoked.
     *      If you are diligent enough to make better version
     *      of this func, feel free to do so.
     *
     * @param new List<TData> New data set.
     */
    open fun updateItems(new: List<TData>) {
        _currentItems.clear()
        _currentItems.addAll(new)
        notifyDataSetChanged()
    }

    /**
     * Load light/dark colors from res.
     */
    protected companion object {
        var lightColor: Int = ContextCompat.getColor(MyApp.appContext, R.color.list_dark)
        var darkColor: Int = ContextCompat.getColor(MyApp.appContext, R.color.list_light)
    }
}