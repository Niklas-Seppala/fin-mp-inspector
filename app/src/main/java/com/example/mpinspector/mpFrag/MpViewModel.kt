package com.example.mpinspector.mpFrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.MemberOfParliamentModel
import kotlinx.coroutines.launch

class MpViewModel : ViewModel() {
    private val _mp = MutableLiveData<MemberOfParliamentModel>()
    val currentMp = _mp as LiveData<MemberOfParliamentModel>

    fun load(position: Int) {
        viewModelScope.launch {
            val mps = Repository.instance.getMembersOfParliament().value
            if (mps != null) {
                _mp.value = mps[position]
            }
        }
    }
}