package com.example.mpinspector.ui.mplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.MpModel
import java.security.InvalidParameterException

class MpListViewModel: ViewModel() {
    companion object Party {
        val map = mapOf(
            R.id.chipKok to "kok",
            R.id.chipKesk to "kesk",
            R.id.chipKd to "kd",
            R.id.chipLiik to "liik",
            R.id.chipPs to "ps",
            R.id.chipSd to "sd",
            R.id.chipVas to "vas",
            R.id.chipVihr to "vihr",
            R.id.chipR to "r"
        )
    }

    val mps: LiveData<List<MpModel>> = Repository.mps.getMps()

    private val _partyFilter = MutableLiveData(Party.map.map { it.value }.toMutableSet())
    val partyFilter: LiveData<MutableSet<String>>
        get() = _partyFilter

    fun partyChipClicked(resId: Int, checked: Boolean) {
        val party = Party.map[resId]
            ?: throw InvalidParameterException("Chip id not mapped to party.")

        _partyFilter.value?.let {
            if (checked) it.add(party) else it.remove(party)
            val newSet = mutableSetOf<String>()
            newSet.addAll(it)
            _partyFilter.value = newSet // Invoke onChange CB.
        }
    }
}