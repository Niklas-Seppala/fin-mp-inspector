package com.example.mpinspector.ui.mplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mpinspector.MyApp
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentItemBinding

import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.ui.adapters.GenericAdapter
import com.example.mpinspector.ui.adapters.ViewHolder
import com.example.mpinspector.utils.PartyMapper

class MpAdapter(items: List<MpModel>): GenericAdapter<MpModel, FragmentItemBinding>(items) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.binding as FragmentItemBinding
        val current = currentItems[position]

        holder.binding.content.text = MyApp.appContext.getString(R.string.mpFragFullName, current.first, current.last)
        holder.binding.partyLogoIv.setImageResource(PartyMapper.partyIcon(current.party))

        if (current.minister) {
            holder.binding.imageView3.setImageResource(R.drawable.ic_minister)
            holder.binding.imageView3.visibility = View.VISIBLE
        } else {
            holder.binding.imageView3.visibility = View.GONE
            holder.binding.imageView3.setImageDrawable(null)
        }

        holder.itemView.setOnClickListener {
            when (holder.binding.root.findNavController().currentDestination?.id ?: -1) {

                R.id.nav_fav_mps -> navigateTo(holder.binding.root.findNavController(),
                    FavoriteMpListFragmentDirections.actionNavFavMpsToNavMpInspect()) {
                    it.mpId = current.personNumber
                }

                R.id.nav_mp_list -> navigateTo(holder.binding.root.findNavController(),
                    MpListItemFragmentDirections.actionMpListItemFragmentToMpFragment()) {
                    it.mpId = current.personNumber
                }
            }
        }
    }

    fun filter(parties: Set<String>, text: String) {
        updateItems(items.filter { it.party in parties }
            .filter { "${it.first} {$it.last}".contains(text, ignoreCase = true) }
            .toList())
    }

    override fun createBinding(parent: ViewGroup): FragmentItemBinding {
        return FragmentItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
    }
}
