package com.example.mpinspector.ui.twitter

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.MpTwitterModel
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.repository.mps.ImageSize
import com.example.mpinspector.ui.mplist.combineWith
import kotlinx.coroutines.async
import java.time.Instant

class TweetRenderData(val mpTweets: List<Pair<MpTwitterModel, TweetModel>>, val imgMap: Map<Int, Bitmap>)

class TwitterFeedViewModel : ViewModel() {

    // First get the mps who are in twitter feed.
    private val mps = Repository.mps.getMpsFromTwitterFeed()
    private val read = Transformations.switchMap(mps) { Repository.twitter.getReadTweetIds() }
    val asd = Transformations.switchMap(read) { read.combineWith(mps) {a, b -> a to b } }

    val tweets = Transformations.switchMap(asd) {
        liveData {
            // Launch concurrent tasks to fetch tweets and mp images.
            val mps = it.second !!
            val read = it.first !!

            // Create a task to fetch tweets for each mp and flatten results
            // to single list of mp to tweet pairs.
            val tweets = viewModelScope.async {
                mps.flatMap { mp ->
                     Repository.twitter.getTweets(mp.twitterId ?: "")
                         .filter { it.id !in read }
                         .map { mp to it }
                }
            }

            // Create a task to create a map from mp.personNumbers to Bitmaps
            val images = viewModelScope.async {
                mps.map {
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

