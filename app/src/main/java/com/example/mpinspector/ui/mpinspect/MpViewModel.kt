package com.example.mpinspector.ui.mpinspect

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MemberOfParliamentModel
import com.example.mpinspector.utils.PartyMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.RuntimeException
import java.util.*

class MpViewModel() : ViewModel() {
    private companion object {
        private val year = Calendar.getInstance().get(Calendar.YEAR)
    }

    private lateinit var _image: Bitmap
    val image: Bitmap
        get() = _image

    private var _partyIcon: Int = -1
    val partyIcon
        get() = _partyIcon

    private var _comments = MutableLiveData<MutableList<CommentModel>>()
    val comments: LiveData<MutableList<CommentModel>>
        get() =_comments

    private val _mp = MutableLiveData<MemberOfParliamentModel>()
    val currentMp = _mp as LiveData<MemberOfParliamentModel>

    val mpAge: Int
        get() = year - (_mp.value?.bornYear ?: 0)

    fun addComment(comment: CommentModel) {
        _comments.value ?: return
        _comments.value?.add(comment)
        viewModelScope.launch {
            Repository.instance.mps.insertMpComment(comment)
        }
    }

    suspend fun load(position: Int?) {
        position ?: throw IllegalArgumentException("Position was invalid, no mp could be loaded")
        val mps = Repository.instance.mps.getMembersOfParliament()

        val imageTask = viewModelScope.async {
            _partyIcon = PartyMapper.partyIcon((mps[position].party))
        }
        val iconTask = viewModelScope.async {
            _image = Repository.instance.mps.getMpImage(mps[position].personNumber)
        }
        _mp.value = mps[position]

        _comments = Repository.instance
            .mps.getMpComments(_mp.value?.personNumber ?: return)      // FAIL POINT

        awaitAll(imageTask, iconTask)
    }
}