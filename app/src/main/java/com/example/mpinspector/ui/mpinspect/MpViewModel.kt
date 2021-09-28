package com.example.mpinspector.ui.mpinspect

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.utils.PartyMapper
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import java.util.*

class MpViewModel() : ViewModel() {
    private companion object {
        private val year = Calendar.getInstance().get(Calendar.YEAR)
    }

    private var _image: Bitmap? = null
    val image: Bitmap?
        get() = _image

    private var _partyIcon: Int = -1
    val partyIcon
        get() = _partyIcon

    private var _isFavorite = false
    val isFavorite: Boolean
        get() = _isFavorite

    private var _favBtnImg = MutableLiveData<Int>()
    val favBtnImg: LiveData<Int>
        get() = _favBtnImg

    private var _comments = MutableLiveData<MutableList<CommentModel>>()
    val comments: LiveData<MutableList<CommentModel>>
        get() = _comments

    private val _mp = MutableLiveData<MpModel>()
    val currentMp: LiveData<MpModel>
        get() = _mp

    val mpAge: Int
        get() = year - (_mp.value?.bornYear ?: 0)

    fun addComment(comment: CommentModel) {
        _comments.value?.apply {
            viewModelScope.launch { Repository.mps.insertMpComment(comment) }
            add(comment)
        }
    }

    fun favBtnPressed() {
        _isFavorite = !_isFavorite
        toggleFavIcon()
    }

    private fun toggleFavIcon() {
        _favBtnImg.value =
            if (_isFavorite) R.drawable.ic_star
            else R.drawable.ic_star_outline
    }

    fun load(mpId: Int?) {
        mpId ?: throw IllegalArgumentException("Id cant be null")
        viewModelScope.launch {
            val mp = Repository.mps.getMemberOfParliament(mpId)
            val image = Repository.mps.getMpImage(mpId)
            val isFav = Repository.mps.getFavorites().any { it.mpId == mp.personNumber }
            val comments = Repository.mps.getMpComments(mp.personNumber)  // FAIL POINT

            _image = image
            _partyIcon = PartyMapper.partyIcon((mp.party))
            _isFavorite = isFav
            toggleFavIcon()
            _comments.value = comments
            _mp.value = mp
        }
    }
}