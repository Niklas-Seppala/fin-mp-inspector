package com.example.mpinspector.ui.mplist

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentItemListBinding
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.MpModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class MpListViewModel: ViewModel() {
    val mps: LiveData<List<MpModel>> = Repository.mps.getMps()
}

class MpListItemFragment : Fragment() {
    private lateinit var binding: FragmentItemListBinding
    private lateinit var checkedParties: MutableList<String>
    private lateinit var listAdapter: MpAdapter
    private lateinit var viewModel: MpListViewModel

    private val partyMap = mapOf(
        R.id.chipKok    to "kok",
        R.id.chipKesk   to "kesk",
        R.id.chipKd     to "kd",
        R.id.chipLiik   to "liik",
        R.id.chipPs     to "ps",
        R.id.chipSd     to "sd",
        R.id.chipVas    to "vas",
        R.id.chipVihr   to "vihr",
        R.id.chipR      to "r"
    )

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_item_list, cont, false)
        binding.list.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProvider(this).get(MpListViewModel::class.java)
        listAdapter = MpAdapter(listOf())
        binding.list.adapter = listAdapter


        viewModel.mps.observe(viewLifecycleOwner, {
            listAdapter.loadItems(it)
            binding.progressBar2.visibility = View.GONE
        })

        mapCheckedChipsToParties(binding.chips)

        binding.editTextTextPersonName.doAfterTextChanged {
            val mps = viewModel.mps.value ?: return@doAfterTextChanged
            listAdapter.setFiltered(filteredMps(mps, checkedParties, it))
        }

        setupFiltering()
        return binding.root
    }

    private fun setupFiltering() {
        for (child in binding.chips.children) {
            (child as Chip).setOnCheckedChangeListener { view, checked ->
                val mps = viewModel.mps.value ?: return@setOnCheckedChangeListener
                val text = binding.editTextTextPersonName.text
                val party = partyMap[view.id] ?: return@setOnCheckedChangeListener
                if (checked) {
                    checkedParties.add(party)
                } else {
                    checkedParties.remove(party)
                }
                listAdapter.setFiltered(filteredMps(mps, checkedParties, text))
            }
        }
    }

    private fun mapCheckedChipsToParties(group: ChipGroup) {
        checkedParties = group
            .checkedChipIds
            .mapNotNull { partyMap[it] }
            .toMutableList()
    }

    private fun filteredMps(mps: List<MpModel>, active: List<String>,
                            text: Editable?) : List<MpModel> {
        return mps.filter { active.contains(it.party) }
            .filter { "${it.first} ${it.last}"
                .contains(text.toString(), ignoreCase = true)
            }
    }
}