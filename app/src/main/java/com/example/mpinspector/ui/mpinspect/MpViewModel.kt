package com.example.mpinspector.ui.mpinspect

import android.text.Editable
import androidx.lifecycle.*
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.utils.MyTime
import kotlinx.coroutines.launch
import java.util.*

class MpViewModel(var mpId: Int) : ViewModel() {
    private companion object {
        private val year = Calendar.getInstance().get(Calendar.YEAR)
    }
    val mp = Repository.mps.getMp(mpId)
    val comments = Repository.mps.getMpComments(mpId)
    val image = liveData { emit(Repository.mps.getMpImage(mpId)) }
    private val _favBtnImg = MutableLiveData<Int>()
    val favoriteButtonImage: LiveData<Int>
        get() = _favBtnImg
    private var _isFavorite: Boolean = false
    val isFavorite: Boolean
        get() = _isFavorite

    init {
        viewModelScope.launch {
            _isFavorite = Repository.mps.getFavorites().any { it.mpId == mpId }
            _favBtnImg.value =
                if (_isFavorite) R.drawable.ic_star
                else R.drawable.ic_star_outline
        }
    }

    val loadComplete = MutableLiveData(false)
    var mpLoaded = false
        set(value) {
            field = value
            loadComplete.value = mpLoaded && imageLoaded
        }
    var imageLoaded = false
        set(value) {
            field = value
            loadComplete.value = mpLoaded && imageLoaded
        }

    val mpAge: Int
        get() = year - (mp.value?.bornYear ?: 0)

    val fullName: String
        get() = "${mp.value?.first} ${mp.value?.last}"


    fun addComment(note: CommentModel) {
        viewModelScope.launch {
            Repository.mps.insertMpComment(note)
        }
    }

    fun favoriteButtonClick() {
        viewModelScope.launch {
            val fav = FavoriteModel(mpId, MyTime.timestampLong)
            if (isFavorite) {
                Repository.mps.removeFavMp(fav)
            } else {
                Repository.mps.addFavMp(fav)
            }
        }
        _isFavorite = !_isFavorite
        _favBtnImg.value =
            if (_isFavorite) R.drawable.ic_star
            else R.drawable.ic_star_outline
    }
}
