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
import com.example.mpinspector.repository.models.MpModel
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

        adapter = MpAdapter(listOf(), this)
        binding.mpList.adapter = adapter

        viewModel.mps.observe(viewLifecycleOwner, {
            adapter.setNewBaseItems(it)
            binding.loadingSpinner.visibility = View.GONE
        })
        viewModel.partyFilter.observe(viewLifecycleOwner, { parties ->
            adapter.filter(parties, binding.personName.text.toString())
        })

        setSearchListener()
        setChipCheckListeners()

        return binding.root
    }

    private fun setSearchListener() {
        binding.personName.doAfterTextChanged {
            adapter.filter(
                viewModel.partyFilter.value
                    ?: MpListViewModel.partyMap.values.toSet(),
                it.toString()
            )
        }
    }

    private fun setChipCheckListeners() {
        binding.chips.children.filterIsInstance<Chip>().forEach {
            it.setOnCheckedChangeListener { view, checked ->
                viewModel.partyChipClicked(view.id, checked)
            }
        }
    }
}