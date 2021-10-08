package com.example.mpinspector.ui.mpinspect

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpWithComments
import com.example.mpinspector.repository.models.TwitterFeedModel
import com.example.mpinspector.repository.mps.ImageSize
import com.example.mpinspector.utils.MyTime
import kotlinx.coroutines.launch
import java.util.*

data class MpInspectBundle(val mpWithComments: MpWithComments, val image: Bitmap)

class MpViewModel(var mpId: Int) : ViewModel() {
    private val mpWithComments = Repository.mps.getMpWithComments(mpId)
    val mpInspectBundle = Transformations.switchMap(mpWithComments) {
        liveData {
            val image = Repository.mps.getMpImage(it.mp.personNumber, ImageSize.NORMAL)
            emit(MpInspectBundle(it, image))
        }
    }

    val toastMessage = MutableLiveData<String>()
    val isFavLiveData = Repository.mps.isMpInFavorites(mpId)

    private val isTwitterLiveData = Repository.twitter.isMpInTwitterFeed(mpId)
    private val doesMpHaveTwitter = liveData { emit(Repository.twitter.mpHasTwitter(mpId)) }
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
            loadComplete.value = mpLoaded
        }

    fun commentOkButtonClick (commentText: String, like: Boolean) {
        mpInspectBundle.value?.let {
            val comment = CommentModel(0, it.mpWithComments.mp.personNumber, commentText, like, MyTime.timestampLong)
            viewModelScope.launch { Repository.mps.storeMpComment(comment) }
        }
    }

    fun favoriteButtonClick() {
        viewModelScope.launch {
            val fav = FavoriteModel(mpId, MyTime.timestampLong)
            val name = mpWithComments.value?.mp?.fullName
            if (isFavLiveData.value == true) {
                Repository.mps.deleteFavoriteMp(fav)
                toastMessage.value = "$name removed from your favorites."
            } else {
                Repository.mps.storeFavoriteMp(fav)
                toastMessage.value = "$name added to your favorites."
            }
        }
    }

    fun twitterButtonClicked() {
        viewModelScope.launch {
            val fav = TwitterFeedModel(mpId)
            val name = mpWithComments.value?.mp?.fullName
            if (isTwitterLiveData.value == true) {
                Repository.twitter.removeMpFromFeed(fav)
                toastMessage.value = "$name removed from your Twitter feed."
            } else {
                Repository.twitter.addMpToTwitterFeed(fav)
                toastMessage.value = "$name added to your Twitter feed."
            }
        }
    }
}
