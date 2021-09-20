package com.example.mpinspector.mpFrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mpinspector.MemberOfParliament
import com.example.mpinspector.ParliamentMembersData
import java.util.*

class MpViewModel : ViewModel() {
    private val mMp = MutableLiveData<MemberOfParliament>()
    val currentMp = mMp as LiveData<MemberOfParliament>

    private fun load(mp: MemberOfParliament) {
        mMp.value = mp
    }

    fun next() {
        val rng = ParliamentMembersData.members.indices
        load(ParliamentMembersData.members[rng.random()])
    }
}