package com.example.mpinspector.ui.twitter

import android.graphics.Bitmap
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentTweetBinding
import com.example.mpinspector.repository.models.MpTwitterModel
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.ui.adapters.GenericAdapter
import java.text.DateFormat
import java.time.Instant

class TweetAdapter(items: List<Pair<MpTwitterModel, TweetModel>>, val images: Map<Int, Bitmap>) :
    GenericAdapter<Pair<MpTwitterModel, TweetModel>, FragmentTweetBinding>(
        items,
        R.layout.fragment_tweet
    ) {

    override fun hookUpItemWithView(
        binding: FragmentTweetBinding,
        item: Pair<MpTwitterModel, TweetModel>
    ) {
        val author = item.first
        val tweet = item.second

        binding.tweetContent.text = tweet.content
        binding.createdAt.text =
            DateFormat.getInstance().format(Instant.parse(tweet.createdAt).epochSecond * 1000)
        binding.tweetAuthor.text =
            binding.root.context.getString(R.string.mpFragFullName, author.first, author.last)
        binding.tweetProfilePic.setImageBitmap(images[author.personNumber])
    }
}