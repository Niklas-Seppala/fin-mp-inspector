package com.example.mpinspector.ui.twitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentTwitterFeedBinding
import com.example.mpinspector.repository.models.MpTwitterModel
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.ui.adapters.OnRecycleViewItemClick
import android.content.Intent
import android.net.Uri

typealias TweetWithAuthor = Pair<MpTwitterModel, TweetModel>

class TwitterFeedFragment : Fragment() {
    private lateinit var binding: FragmentTwitterFeedBinding
    private lateinit var viewModel: TwitterFeedViewModel
    private lateinit var adapter: TweetAdapter

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_twitter_feed, cont, false)
        viewModel = ViewModelProvider(this).get(TwitterFeedViewModel::class.java)

        viewModel.tweets.observe(viewLifecycleOwner, {
            adapter = TweetAdapter(it.mpTweets, it.imgMap, arrayOf(onTwitterBtnClick, onDeleteTweetBtnClick))
            binding.tweetList.adapter = adapter
            binding.twitterFeedSpinner.visibility = View.GONE
        })

        return binding.root
    }

    /**
     * Twitter button click listener object. Starts Browser Intent with clicked
     * tweet as URL.
     */
    private val onTwitterBtnClick = object : OnRecycleViewItemClick<TweetWithAuthor> {
        override fun onItemClick(itemData: TweetWithAuthor) {
            val author = itemData.first
            val tweet = itemData.second

            val uri = Uri.parse("https://twitter.com/${author.username}/status/${tweet.id}")
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }

    /**
     * Tweet delete button click listener object. Deletes clicked item from RecycleView,
     * and updates UI
     */
    private val onDeleteTweetBtnClick = object : OnRecycleViewItemClick<TweetWithAuthor> {
        override fun onItemClick(itemData: TweetWithAuthor) {
            adapter.delete(itemData)
        }
    }

    /**
     * Listener array indexes.
     */
    companion object {
        const val OPEN_IN_TWITTER_LISTENER = 0
        const val DELETE_TWEET_LISTENER = 1
    }
}