package com.example.mpinspector.ui.mplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentItemListBinding
import com.example.mpinspector.ui.adapters.MpAdapter
import com.google.android.material.chip.Chip

class MpListFragment : MpRecycleViewFragment() {
    private lateinit var adapter: MpAdapter
    private lateinit var binding: FragmentItemListBinding
    private lateinit var viewModel: MpListViewModel

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(MpListViewModel::class.java)
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_item_list, cont, false)
        binding.mpList.layoutManager = LinearLayoutManager(context)

        viewModel.mps.observe(viewLifecycleOwner, {
            adapter = MpAdapter(it, this)
            binding.mpList.adapter = adapter
            binding.loadingSpinner.visibility = View.GONE
        })
        viewModel.activeMps.observe(viewLifecycleOwner, { adapter.update(it) })

        binding.personName.doAfterTextChanged { viewModel.searchTextChanged(it.toString()) }
        binding.chips.children.filterIsInstance<Chip>().forEach {
            it.setOnCheckedChangeListener { view, checked ->
                viewModel.partyChipClicked(view.id, checked)
            }
        }

        return binding.root
    }
}