package com.example.mpinspector.ui.mpinspect

import androidx.lifecycle.*
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.utils.MyTime
import com.example.mpinspector.utils.PartyMapper
import kotlinx.coroutines.launch
import java.util.*

class MpViewModel(var mpId: Int) : ViewModel() {
    private companion object {
        private val year = Calendar.getInstance().get(Calendar.YEAR)
    }
    val mpLiveData = Repository.mps.getMp(mpId)
    val ageLiveData: LiveData<Int> = Transformations.map(mpLiveData) { year - it.bornYear }
    val commentsLiveData = Repository.mps.getMpComments(mpId)
    val imageLiveData = liveData { emit(Repository.mps.getMpImage(mpId)) }
    val isFavLiveData = Repository.mps.isMpInFavorites(mpId)
    val iconLiveData: LiveData<Int> = Transformations.map(isFavLiveData) {
        if (it) R.drawable.ic_star
        else R.drawable.ic_star_outline
    }
    val favoriteToast = MutableLiveData<String>()

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
        get() = year - (mpLiveData.value?.bornYear ?: 0)

    val fullName: String
        get() = "${mpLiveData.value?.first} ${mpLiveData.value?.last}"


    fun commentOkButtonClick (commentText: String) {
        mpLiveData.value?.let {
            val comment = CommentModel(0, it.personNumber, commentText, MyTime.timestampLong)
            viewModelScope.launch { Repository.mps.storeMpComment(comment) }
        }
    }

    fun favoriteButtonClick() {
        viewModelScope.launch {
            val fav = FavoriteModel(mpId, MyTime.timestampLong)
            if (isFavLiveData.value == true) {
                Repository.mps.deleteFavoriteMp(fav)
                favoriteToast.value = "$fullName removed from favorites."
            } else {
                Repository.mps.storeFavoriteMp(fav)
                favoriteToast.value = "$fullName added to favorites."
            }
        }
    }
}
