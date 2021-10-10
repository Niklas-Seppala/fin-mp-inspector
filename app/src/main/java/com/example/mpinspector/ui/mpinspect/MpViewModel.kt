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

/**
 * Bundle class for the MpInspect UI view.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
data class MpInspectBundle(val mpWithComments: MpWithComments, val image: Bitmap)

/**
 * ViewModel class for mp inspect view.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 *
 * @property mpId Int Id of the mp this view model models.
 */
class MpViewModel(var mpId: Int) : ViewModel() {
    val toastMessage = MutableLiveData<String>()
    val isFavLiveData = Repository.mps.isMpInFavorites(mpId)

    // Get MP with his/hers comments from repository
    private val mpWithComments = Repository.mps.getMpWithComments(mpId)
    // Join that data with MPs image, and create single bundle object to be displayed.
    val mpInspectBundle = Transformations.switchMap(mpWithComments) {
        liveData {
            val image = Repository.mps.getMpImage(it.mp.personNumber, ImageSize.NORMAL)
            emit(MpInspectBundle(it, image))
        }
    }

    // Check from Repository if user is subscribed to MPs twitter feed.
    private val isTwitterLiveData = Repository.twitter.isMpInTwitterFeed(mpId)

    // Check from repository if current MP has twitter or no.
    private val doesMpHaveTwitter = liveData { emit(Repository.twitter.mpHasTwitter(mpId)) }

    // Map twitter button icons.
    val twitterLiveData: LiveData<Int> = Transformations.switchMap(doesMpHaveTwitter) {
        if (!it) Transformations.map(doesMpHaveTwitter) {0}
        else
            Transformations.map(isTwitterLiveData) { active ->
                if (active) R.drawable.ic_twitter_filled
                else R.drawable.ic_twitter_outline
            }
    }

    // This field makes sure that the data is presented all at once to the UI.
    val loadComplete = MutableLiveData(false)
    var mpLoaded = false
        set(value) {
            field = value
            loadComplete.value = mpLoaded
        }

    /**
     * Submits new comment object to Repository under MP.
     */
    fun submitComment (commentText: String, like: Boolean) {
        val currentTime = MyTime.timestampLong
        val comment = CommentModel(0, mpId, commentText, like, currentTime)
        viewModelScope.launch {
            Repository.mps.storeMpComment(comment)
        }
    }

    /**
     * Adds/Removes MP from favorites and displays Toast message of
     * committed operation.
     */
    fun favoriteButtonClick() {
        viewModelScope.launch {
            val fav = FavoriteModel(mpId, MyTime.timestampLong)
            val name = mpWithComments.value?.mp?.fullName

            if (isFavLiveData.value == true) {
                // Remove MP from favorites.
                Repository.mps.deleteFavoriteMp(fav)
                toastMessage.value = "$name removed from your favorites."
            } else {
                // Add MP to favorites.
                Repository.mps.addFavoriteMp(fav)
                toastMessage.value = "$name added to your favorites."
            }
        }
    }

    /**
     * Adds/Removes MP from twitter feed and displays Toast message
     * of committed operation.
     */
    fun twitterButtonClicked() {
        viewModelScope.launch {
            val twitterFeed = TwitterFeedModel(mpId, null)
            val name = mpWithComments.value?.mp?.fullName

            if (isTwitterLiveData.value == true) {
                // Remove this MP from twitter feed.
                Repository.twitter.removeMpFromFeed(twitterFeed)
                toastMessage.value = "$name removed from your Twitter feed."
            } else {
                // Add this MP to twitter feed.
                Repository.twitter.addMpToTwitterFeed(twitterFeed)
                toastMessage.value = "$name added to your Twitter feed."
            }
        }
    }
}
