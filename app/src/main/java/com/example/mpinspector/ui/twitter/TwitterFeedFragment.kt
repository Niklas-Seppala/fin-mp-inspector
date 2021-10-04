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
import androidx.navigation.fragment.findNavController
import com.example.mpinspector.ui.NavActions

typealias TweetWithAuthor = Pair<MpTwitterModel, TweetModel>

class TwitterFeedFragment : Fragment() {
    private lateinit var binding: FragmentTwitterFeedBinding
    private lateinit var viewModel: TwitterFeedViewModel
    private lateinit var adapter: TweetAdapter

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_twitter_feed, cont, false)
        viewModel = ViewModelProvider(this).get(TwitterFeedViewModel::class.java)

        adapter = TweetAdapter(listOf(), mapOf(),
            arrayOf(onTwitterBtnClick, onDeleteTweetBtnClick, onProfileClick))
        binding.tweetList.adapter = adapter

        viewModel.tweets.observe(viewLifecycleOwner,
            { adapter.updateWithImages(it.mpTweets, it.imgMap) })

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
     * Tweet profile click listener object. Navigates to MpInspect.
     */
    private val onProfileClick = object : OnRecycleViewItemClick<TweetWithAuthor> {
        override fun onItemClick(itemData: TweetWithAuthor) {
            val nav = findNavController()
            val action = NavActions.fromTwitterFeedToInspect
            action.mpId = itemData.first.personNumber
            nav.navigate(action)
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
        const val INSPECT_PROFILE_LISTENER = 2
    }
}