package com.example.mpinspector.ui.twitter

import androidx.lifecycle.*
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.mps.ImageSize
import kotlinx.coroutines.launch

/**
 * ViewModel class for twitter feed view.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class TwitterFeedViewModel : ViewModel() {
    val updating: MutableLiveData<Boolean> = MutableLiveData(false)

    // Check if feed size is zero, set empty message based on that.
    private val feedSize = Repository.twitter.getTwitterFeedSize()
    val emptyMessage = Transformations.map(feedSize) {
        if (it == 0)  "Add MPs to your twitter feed" else  "You are up to date."
    }

    // Fetch all not yet read tweets from Repository. Bundle author image
    // to each tweet.
    private val tweets = Repository.twitter.getTweets()
    val tweetsWithImages = Transformations.switchMap(tweets) {
        liveData {
            val imageMap = it.map { it.authorId }.toSet()
                .map { it to  Repository.mps.getMpImage(it, ImageSize.SMALL, 15)}
                .toMap()
            emit( it.map { TweetBundle(it, imageMap[it.authorId]!!)  })
        }
    }

    fun loadLatestTweets() {
        updating.value = true
        viewModelScope.launch {
            Repository.twitter.loadLatestTweets()
            updating.postValue(false)
        }
    }
}

