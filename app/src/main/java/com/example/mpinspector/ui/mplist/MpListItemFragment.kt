package com.example.mpinspector.ui.mplist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentItemListBinding
import com.google.android.material.chip.Chip

class MpListItemFragment : Fragment() {
    private lateinit var binding: FragmentItemListBinding
    private lateinit var listAdapter: MpAdapter
    private lateinit var viewModel: MpListViewModel

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_item_list, cont, false)
        binding.list.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProvider(this).get(MpListViewModel::class.java)

        viewModel.mps.observe(viewLifecycleOwner, {
            listAdapter = MpAdapter(it)
            binding.list.adapter = listAdapter
            binding.progressBar2.visibility = View.GONE
            viewModel.partyFilter.observe(viewLifecycleOwner, { parties ->
                listAdapter.performFiltering(parties, binding.personName.text.toString())
            })
        })

        binding.personName.doAfterTextChanged {
            listAdapter.performFiltering(viewModel.partyFilter.value
                ?: return@doAfterTextChanged, it.toString())
        }

        binding.chips.children.filterIsInstance<Chip>().forEach {
            it.setOnCheckedChangeListener { view, checked ->
                viewModel.partyChipClicked(view.id, checked)
            }
        }

        return binding.root
    }
}