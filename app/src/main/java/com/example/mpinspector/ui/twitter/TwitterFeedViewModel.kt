package com.example.mpinspector.ui.twitter

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.mps.ImageSize
import java.time.Instant

class TwitterFeedViewModel : ViewModel() {
    private val mps = Repository.mps.getMpsFromTwitterFeed()

    val tweets = Transformations.switchMap(mps) {
        liveData {
            emit(it.flatMap { mp ->
                Repository.twitter.getTweets(
                    mp.twitterId ?: "0" // Mps are selected from db based on twitter id not being null.
                ).map { tweet -> mp to tweet }
            }.sortedBy { Instant.parse(it.second.createdAt).epochSecond })
        }
    }

    val images = Transformations.switchMap(mps) {
        liveData {
            emit(it.map { mp ->
                mp.personNumber to Repository.mps.getMpImage(
                    mp.personNumber,
                    ImageSize.SMALL,
                    15
                )
            }.toMap())
        }
    }
}