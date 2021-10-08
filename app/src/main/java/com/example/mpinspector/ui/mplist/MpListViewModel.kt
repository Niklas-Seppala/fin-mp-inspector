package com.example.mpinspector.ui.mplist

import androidx.lifecycle.*
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.MpModel
import java.security.InvalidParameterException

fun <T, K, R> LiveData<T>.combineWith(liveData: LiveData<K>, block: (T?, K?) -> R): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) { result.value = block(this.value, liveData.value) }
    result.addSource(liveData) { result.value = block(this.value, liveData.value) }
    return result
}

class MpListViewModel: ViewModel() {

    // Live data of all of the Mps from database.
    val mps: LiveData<List<MpModel>> = Repository.mps.getMps()

    // Live data for typed search query.
    private val searchText = Transformations.switchMap(mps) {
        MutableLiveData("")
    }  as MutableLiveData<String>

    // Live data for active party filters.
    private val _partyFilter = Transformations.switchMap(mps) {
        MutableLiveData(partyMap.map { it.value }.toMutableSet())
    } as MutableLiveData<MutableSet<String>>

    // Live data of combined query (text and party filters).
    val queryChange = searchText.combineWith(_partyFilter) { text, partyFilter -> text to partyFilter }

    // Live data of the current mps (after filtering).
    val active = Transformations.map(queryChange) {
        val text = it.first
        val parties = it.second

        // for smart casts
        if (text == null || parties == null) return@map listOf<MpModel>()

        mps.value?.let { mps ->
            mps.filter { mp ->
                val name = "${mp.first} ${mp.last}"
                mp.party in parties && name.contains(text, ignoreCase = true)
            }
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