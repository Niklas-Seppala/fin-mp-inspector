package com.example.mpinspector

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.example.mpinspector.databinding.FragmentItemBinding
import com.example.mpinspector.utils.PartyMapper

class MpItemRecyclerViewAdapter(private val values: List<MemberOfParliament>)
    : RecyclerView.Adapter<MpItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = "${item.first} ${item.last}"

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, PartyMapper.partyName(item.party), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
    }
}