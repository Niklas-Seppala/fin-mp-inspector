package com.example.mpinspector.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.mpinspector.MyApp
import com.example.mpinspector.R

import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.utils.MpItemBinding
import com.example.mpinspector.utils.PartyMapper

/**
 * RecycleView Adapter for Mps
 */
class MpAdapter (items: List<MpModel>, listener: OnRecycleViewItemClick<MpModel>)
    : GenericAdapter<MpModel, MpItemBinding>(items, R.layout.fragment_list_mp_item, listener) {

    override fun hookUpItemWithView(binding: MpItemBinding, item: MpModel) {
        binding.content.text = MyApp.appContext.getString(R.string.mpFragFullName, item.first, item.last )
        binding.partyLogoIv.setImageResource(PartyMapper.partyIcon(item.party))
        binding.imageView3.setImageResource(
            if (item.minister) R.drawable.ic_minister else (0)
        )
    }

    override val diffComparator: DiffComparator<MpModel> = { a, b -> a.personNumber == b.personNumber }
}
