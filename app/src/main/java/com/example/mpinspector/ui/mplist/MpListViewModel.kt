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

/**
 * ViewModel class for MpList view.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
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
    private val queryChange = searchText.combineWith(_partyFilter) { text, partyFilter -> text to partyFilter }

    // Live data of the current mps (after filtering).
    val active = Transformations.map(queryChange) {
        val text = it.first
        val parties = it.second

        // for smart casts
        if (text == null || parties == null) return@map listOf<MpModel>()

        mps.value?.let { mps ->
            mps.filter { mp ->
                mp.party in parties && mp.fullName.contains(text, ignoreCase = true)
            }
        }
    }

    /**
     * Updates Search text LiveData
     *
     * @param new String Latest search text.
     */
    fun searchTextChanged(new: String) {
        searchText.value = new
    }

    /**
     * Updates party filter LiveData based on happened
     * change in party Chips.
     *
     * @param viewId Int changed Chip's id.
     * @param checked Boolean Is the clicked Chip active
     */
    fun partyChipClicked(viewId: Int, checked: Boolean) {
        val party = partyMap[viewId] ?: throw InvalidParameterException("Chip id not mapped to party.")

        _partyFilter.value?.let {
            if (checked) it.add(party) else it.remove(party)
            val newSet = mutableSetOf<String>()
            newSet.addAll(it)
            _partyFilter.value = newSet // Invoke onChange CB.
        }
    }

    // Party filtering chips mapped to party ids.
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