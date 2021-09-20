package com.example.mpinspector.mpFrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mpinspector.MemberOfParliament
import com.example.mpinspector.ParliamentMembersData

class MpViewModel : ViewModel() {
    private val _mp = MutableLiveData<MemberOfParliament>()
    val currentMp = _mp as LiveData<MemberOfParliament>

    /**
     * Load random mp
     */
    fun next() {
        val rng = ParliamentMembersData.members.indices
        _mp.value = ParliamentMembersData.members[rng.random()]
    }
}