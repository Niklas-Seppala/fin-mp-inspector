package com.example.mpinspector.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mpinspector.App
import com.example.mpinspector.R

/**
 * Jack of all trades RecycleViewAdapter base class with generic data and binding.
 * Provide list item type and ViewDataBinding type with related item layout resId.
 * RecycleView item clicks will be delegated to optional OnRecycleViewItemClick
 * listener object. If you instead wish to have more precise click listeners,
 * pass in otherListeners parameter.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 *
 * @param TData List item Type.
 * @param TBinding : ViewDataBinding Type of the item layout ViewDataBiding.
 * @property items List<TData> All start items.
 * @property itemLayoutRes Int Item layout resId.
 * @property colorEveryOtherItem: Boolean Use dark/light colors in view holders.
 * @property onItemClick OnRecycleViewItemClick<TData>? optional click listener.
 * @property otherListeners Array<OnRecycleViewItemClick<TData>>? More precise click listeners.
 * @property currentItems List<TData> Currently displayed items.
 */
abstract class GenericAdapter<TData, TBinding : ViewDataBinding>(
    protected var items: List<TData>,
    private val itemLayoutRes: Int,
    private val colorEveryOtherItem: Boolean = true,
    private val onItemClick: OnRecycleViewItemClick<TData>? = null,
    private val otherListeners: Array<OnRecycleViewItemClick<TData>>? = null
) : RecyclerView.Adapter<GenericViewHolder<TData, TBinding>>() {

    private val _currentItems = items.toMutableList()
    val currentItems: List<TData>
        get() = _currentItems

    override fun getItemCount(): Int = _currentItems.size

    /**
     * Binds any list item specific click listeners to view holder.
     */
    protected open fun bindAdditionalListeners(
        otherListeners: Array<OnRecycleViewItemClick<TData>>?,
        viewHolder: GenericViewHolder<TData, TBinding>
    ) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): GenericViewHolder<TData, TBinding> {
        val holder: GenericViewHolder<TData, TBinding> = GenericViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                itemLayoutRes, parent, false
            ), _currentItems, onItemClick
        )

        // Bind any additional click listeners to ViewHolder.
        bindAdditionalListeners(otherListeners, holder)
        return holder
    }

    override fun onBindViewHolder(holder: GenericViewHolder<TData, TBinding>, position: Int) {
        // Set every other item background dark/light
        if (colorEveryOtherItem) {
            holder.binding.root.setBackgroundColor(
                if (position % 2 == 0) darkColor
                else lightColor
            )
        }
        // Abstract func call
        bindDataToView(holder.binding, _currentItems[position])
    }

    /**
     * Wire the item data to view any way you like it.
     * @param binding TBinding Binding object that holds layouts views.
     * @param item TData Item to operate.
     */
    protected abstract fun bindDataToView(binding: TBinding, item: TData)

    /**
     * Comparison predicate used by DiffUtils. Override this value if you wish
     * to use base version of update(List<TData>) method.
     */
    open val diffCompare: ((TData, TData) -> Boolean)? = null

    /**
     * Updates the current adapter dataset using DiffUtils.
     * @param new List<TData> New dataset for this adapter.
     */
    open fun update(new: List<TData>) {
        val res = DiffUtil.calculateDiff(DiffUtilCb(_currentItems, new))
        _currentItems.clear()
        _currentItems.addAll(new)
        res.dispatchUpdatesTo(this)
    }

    /**
     * Deletes the item in adapter's dataset at provided
     * position and updates UI.
     * @param pos Int Removed item's position index.
     */
    open fun delete(pos: Int) {
        _currentItems.removeAt(pos)
        notifyItemRemoved(pos)
    }

    /**
     * Deletes specified item from adapter's dataset and updates UI.
     * @param item TData Removed item.
     */
    open fun delete(item: TData) {
        val index = _currentItems.indexOf(item)
        if (index >= 0)
            _currentItems.removeAt(index)
        notifyItemRemoved(index)
    }

    /**
     * DiffUtil callback class for generic adapter.
     * Utilizes diffCompare lambda-function override
     * in the base class to perform comparison between generic data types.
     */
    private inner class DiffUtilCb(
        private val old: List<TData>, private val new: List<TData>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return diffCompare?.invoke(old[oldItemPosition], new[newItemPosition])
                ?: throw NullPointerException("Override diffCompare before calling update()")
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return diffCompare?.invoke(old[oldItemPosition], new[newItemPosition])
                ?: throw NullPointerException("Override diffCompare before calling update()")
        }
    }

    private companion object {
        /**
         * Light background color variant.
         */
        val lightColor: Int = ContextCompat.getColor(App.appContext, R.color.list_dark)

        /**
         * Dark background color variant.
         */
        val darkColor: Int = ContextCompat.getColor(App.appContext, R.color.list_light)
    }
}