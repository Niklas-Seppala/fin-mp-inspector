package com.example.mpinspector.ui.mplist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentItemBinding

import com.example.mpinspector.repository.models.MemberOfParliamentModel
import com.example.mpinspector.utils.PartyMapper

class MpItemRecyclerViewAdapter(private val items: List<MemberOfParliamentModel>)
    : RecyclerView.Adapter<MpItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.contentView.text = holder.itemView.context.getString(R.string.mpFragFullName, item.first, item.last)
        holder.partyLogoIv.setImageResource(PartyMapper.partyIcon(item.party))

        holder.itemView.setOnClickListener {
            val action = MpListItemFragmentDirections.actionMpListItemFragmentToMpFragment()
            action.mpIndex = position
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(binding: FragmentItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
        val partyLogoIv: ImageView = binding.partyLogoIv
    }
}