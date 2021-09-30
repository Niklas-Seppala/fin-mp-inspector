package com.example.mpinspector.ui.mplist

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentItemBinding

import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.ui.NavActions
import com.example.mpinspector.ui.adapters.ClickableViewHolder
import com.example.mpinspector.ui.adapters.GenericAdapter
import com.example.mpinspector.ui.adapters.ViewHolder
import java.lang.NullPointerException
import java.lang.RuntimeException

class MpAdapter(items: List<MpModel>, listener: OnMyItemClick) :
    GenericAdapter<MpModel, FragmentItemBinding>(items, listener) {
    override fun onBindViewHolder(holder: ClickableViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        (holder.binding as FragmentItemBinding).mp = currentItems[position]
        holder.binding.mp = currentItems[position]
    }

    fun filter(parties: Set<String>, text: String) {
        updateItems(items.filter { it.party in parties }
            .filter { "${it.first} {$it.last}".contains(text, ignoreCase = true) }
            .toList())
    }

    override fun createBinding(parent: ViewGroup): FragmentItemBinding {
        return FragmentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    }
}
