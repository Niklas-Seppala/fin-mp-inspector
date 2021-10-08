package com.example.mpinspector.ui.twitter

import androidx.lifecycle.*
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.mps.ImageSize
import kotlinx.coroutines.launch

class TwitterFeedViewModel : ViewModel() {
    private val feedSize = Repository.twitter.twitterFeedSize()
    val emptyMessage = Transformations.map(feedSize) {
        if (it == 0)  "Add MPs to your twitter feed" else  "You are up to date."
    }

    private val tweets = Repository.twitter.getAllNotYetRead()
    val tweetsWithImages = Transformations.switchMap(tweets) {
        liveData {
            val imageMap = it.map { it.authorId }.toSet()
                .map { it to  Repository.mps.getMpImage(it, ImageSize.SMALL, 15)}
                .toMap()
            emit( it.map { TweetWithImage(it, imageMap[it.authorId]!!)  })
        }
    }

    val updating: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        loadNewTweets()
    }


    fun loadNewTweets() {
        updating.value = true
        viewModelScope.launch {
            Repository.twitter.getNewTweets()
            updating.postValue(false)
        }
    }
}

