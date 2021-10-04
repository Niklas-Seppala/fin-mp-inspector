package com.example.mpinspector.ui.twitter

import android.graphics.Bitmap
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.MpTwitterModel
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.repository.mps.ImageSize
import kotlinx.coroutines.async
import java.time.Instant

class TweetRenderData (val mpTweets: List<Pair<MpTwitterModel, TweetModel>>, val imgMap: Map<Int, Bitmap>)

class TwitterFeedViewModel : ViewModel() {

    // First get the mps who are in twitter feed.
    private val mps = Repository.mps.getMpsFromTwitterFeed()

    val tweets = Transformations.switchMap(mps) {
        liveData {
            // Launch concurrent tasks to fetch tweets and mp images.

            // Create a task to fetch tweets for each mp and flatten results
            // to single list of mp to tweet pairs.
            val tweets = viewModelScope.async {
                it.flatMap { mp ->
                     Repository.twitter.getTweets(mp.twitterId ?: "").map { mp to it }
                }
            }

            // Create a task to create a map from mp.personNumbers to Bitmaps
            val images = viewModelScope.async {
                it.map {
                    it.personNumber to Repository.mps.getMpImage(it.personNumber, ImageSize.SMALL, 15)
                }.toMap()
            }

            // Await and aggregate results to wrapper class and emit livedata
            emit(TweetRenderData(tweets.await().sortedBy {
                Instant.parse(it.second.createdAt).epochSecond
            }, images.await()))
        }
    }
}

