package com.example.mpinspector.ui.mplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.MpModel
import java.security.InvalidParameterException

class MpListViewModel: ViewModel() {

    val mps: LiveData<List<MpModel>> = Repository.mps.getMps()

    private val searchText = MutableLiveData("")
    private val _partyFilter = Transformations.switchMap(mps) {
        MutableLiveData(partyMap.map { it.value }.toMutableSet())
    } as MutableLiveData<MutableSet<String>>

    val activeMps = Transformations.map(_partyFilter) { pFilter ->
        searchText.value?.let { search ->
            mps.value?.let { mps ->
                mps.filter { mp ->
                    mp.party in pFilter && "${mp.first} ${mp.last}".contains(search)
                }
            } as List<MpModel>
        }
    }

    fun searchTextChanged(new: String) { searchText.value = new }

    fun partyChipClicked(resId: Int, checked: Boolean) {
        val party = partyMap[resId] ?: throw InvalidParameterException("Chip id not mapped to party.")

        _partyFilter.value?.let {
            if (checked) it.add(party) else it.remove(party)
            val newSet = mutableSetOf<String>()
            newSet.addAll(it)
            _partyFilter.value = newSet // Invoke onChange CB.
        }
    }

    companion object Party {
        val partyMap = mapOf(
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
}