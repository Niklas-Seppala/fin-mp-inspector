package com.example.mpinspector.ui.adapters

import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentListMpItemBinding

import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.utils.PartyMapper

/**
 * RecycleView Adapter for Mps.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 *
 * @param items List<MpModel> Dataset of mps.
 * @param listener OnRecycleViewItemClick<MpModel> Click listener.
 */
class MpAdapter(items: List<MpModel>, listener: OnRecycleViewItemClick<MpModel>) :
    GenericAdapter<MpModel, FragmentListMpItemBinding>(
        items,
        R.layout.fragment_list_mp_item,
        colorEveryOtherItem = false,
        onItemClick = listener
    ) {

    override fun bindDataToView(binding: FragmentListMpItemBinding, item: MpModel) {
        binding.content.text = item.fullName
        binding.partyLogoIv.setImageResource(PartyMapper.partyIcon(item.party))
        binding.imageView3.setImageResource(if (item.minister) R.drawable.ic_minister else (0))
    }

    override val diffCompare: (MpModel, MpModel) -> Boolean =
        { a, b -> a.personNumber == b.personNumber }
}
