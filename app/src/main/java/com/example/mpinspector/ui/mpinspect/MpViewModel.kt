package com.example.mpinspector.ui.mpinspect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MemberOfParliamentModel
import kotlinx.coroutines.launch
import java.util.*

class MpViewModel() : ViewModel() {
    private companion object {
        private val year = Calendar.getInstance().get(Calendar.YEAR)
    }

    private var _comments: MutableLiveData<MutableList<CommentModel>>? = null
    val comments: List<CommentModel>?
        get() {
            _comments ?: return null
            return _comments?.value
        }

    private val _mp = MutableLiveData<MemberOfParliamentModel>()
    val currentMp = _mp as LiveData<MemberOfParliamentModel>
    val mpAge = year - (_mp.value?.bornYear ?: 0)


    fun addComment(comment: CommentModel) {
        val stash = _comments?.value ?: return                                       // FAIL POINT
        stash.add(comment)
        _comments?.value = stash // So elegant hack for invoking value changes.
    }

    fun load(position: Int) {
        viewModelScope.launch {
            val mps = Repository.instance.getMembersOfParliament()
            _mp.value = mps[position]

            _comments = Repository.instance
                .getMpComments(_mp.value?.personNumber ?: return@launch)      // FAIL POINT
        }
    }
}