package com.example.mpinspector.ui.mpinspect

import androidx.lifecycle.*
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.TwitterFeedModel
import com.example.mpinspector.repository.mps.ImageSize
import com.example.mpinspector.utils.MyTime
import kotlinx.coroutines.launch
import java.util.*

class MpViewModel(var mpId: Int) : ViewModel() {
    val toastMessage = MutableLiveData<String>()
    val mpLiveData = Repository.mps.getMp(mpId)
    val ageLiveData: LiveData<Int> = Transformations.map(mpLiveData) { year - it.bornYear }
    val commentsLiveData = Repository.mps.getMpComments(mpId)
    val imageLiveData = liveData { emit(Repository.mps.getMpImage(mpId, ImageSize.NORMAL)) }

    private val isFavLiveData = Repository.mps.isMpInFavorites(mpId)
    val iconLiveData: LiveData<Int> = Transformations.map(isFavLiveData) {
        if (it) R.drawable.ic_star
        else R.drawable.ic_star_outline
    }

    private val doesMpHaveTwitter = Repository.twitter.mpHasTwitter(mpId)
    private val isTwitterLiveData = Repository.twitter.isMpInTwitterFeed(mpId)
    val twitterLiveData: LiveData<Int> = Transformations.switchMap(doesMpHaveTwitter) {
        if (!it) Transformations.map(doesMpHaveTwitter) {0}
        else
            Transformations.map(isTwitterLiveData) { active ->
                if (active) R.drawable.ic_twitter_filled
                else R.drawable.ic_twitter_outline
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
        get() = year - (mpLiveData.value?.bornYear ?: 0)

    val fullName: String
        get() = "${mpLiveData.value?.first} ${mpLiveData.value?.last}"


    fun commentOkButtonClick (commentText: String, like: Boolean) {
        mpLiveData.value?.let {
            val comment = CommentModel(0, it.personNumber, commentText, like, MyTime.timestampLong)
            viewModelScope.launch { Repository.mps.storeMpComment(comment) }
        }
    }

    fun favoriteButtonClick() {
        viewModelScope.launch {
            val fav = FavoriteModel(mpId, MyTime.timestampLong)
            if (isFavLiveData.value == true) {
                Repository.mps.deleteFavoriteMp(fav)
                toastMessage.value = "$fullName removed from your favorites."
            } else {
                Repository.mps.storeFavoriteMp(fav)
                toastMessage.value = "$fullName added to your favorites."
            }
        }
    }

    fun twitterButtonClicked() {
        viewModelScope.launch {
            val fav = TwitterFeedModel(mpId)
            if (isTwitterLiveData.value == true) {
                Repository.twitter.removeMpFromFeed(fav)
                Repository.twitter.deleteAllReadByOwner(mpId)
                toastMessage.value = "$fullName removed from your Twitter feed."
            } else {
                Repository.twitter.addMpToTwitterFeed(fav)
                toastMessage.value = "$fullName added to your Twitter feed."
            }
        }
    }

    private companion object {
        private val year = Calendar.getInstance().get(Calendar.YEAR)
    }
}
