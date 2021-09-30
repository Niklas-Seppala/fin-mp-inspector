package com.example.mpinspector.ui.adapters

import com.example.mpinspector.R

import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.utils.MpItemBinding
import com.example.mpinspector.utils.PartyMapper

class MpAdapter (items: List<MpModel>, listener: OnRecycleViewItemClick)
    : ClickableAdapter<MpModel, MpItemBinding>(items, R.layout.fragment_list_mp_item, listener) {

    override fun bind(binding: MpItemBinding, item: MpModel) {
        // It seems data binding in XML works poorly with Recycle View,
        // too much work to find the correct way :(
        binding.content.text = "${item.first} ${item.last}"
        binding.partyLogoIv.setImageResource(PartyMapper.partyIcon(item.party))
        binding.imageView3.setImageResource(if (item.minister) R.drawable.ic_minister else (0)
        )
    }

    fun filter(parties: Set<String>, text: String) {
        updateItems(items.filter { it.party in parties }
            .filter { "${it.first} {$it.last}".contains(text, ignoreCase = true) }
            .toList())
    }
}
