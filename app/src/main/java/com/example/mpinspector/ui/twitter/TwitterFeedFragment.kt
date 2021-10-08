package com.example.mpinspector.ui.twitter

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentTwitterFeedBinding
import com.example.mpinspector.ui.adapters.OnRecycleViewItemClick
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.ui.NavActions
import kotlinx.coroutines.launch

data class TweetWithImage(val tweet: TweetModel, val image: Bitmap)

class TwitterFeedFragment : Fragment() {
    private lateinit var binding: FragmentTwitterFeedBinding
    private lateinit var viewModel: TwitterFeedViewModel
    private lateinit var adapter: TweetAdapter

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_twitter_feed, cont, false)
        viewModel = ViewModelProvider(this).get(TwitterFeedViewModel::class.java)

        adapter = TweetAdapter(activity as Context, listOf(),
            arrayOf(onTwitterBtnClick, onDeleteTweetBtnClick, onProfileClick))
        binding.tweetList.adapter = adapter

        viewModel.emptyMessage.observe(viewLifecycleOwner, {
            binding.emptyListLabel.text = it
        })

        binding.tweetSwipeRefresh.setOnRefreshListener {
            viewModel.loadNewTweets()
        }

        viewModel.updating.observe(viewLifecycleOwner, {
            binding.tweetSwipeRefresh.isRefreshing = it
            binding.tweetList.layoutManager?.scrollToPosition(0)
        })

        viewModel.tweetsWithImages.observe(viewLifecycleOwner, {
            val displayTweets = it.filter { item -> !item.tweet.isRead }
            adapter.update(displayTweets)

            if (displayTweets.isNotEmpty())
                binding.emptyListLabel.visibility = View.GONE
            else
                binding.emptyListLabel.visibility = View.VISIBLE
        })

        ItemTouchHelper(asd).attachToRecyclerView(binding.tweetList)

        return binding.root
    }

    private val asd = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.absoluteAdapterPosition
            if (direction == ItemTouchHelper.LEFT) {
                lifecycleScope.launch { Repository.twitter.markTweetAsRead(
                    adapter.currentItems.get(pos).tweet) }
//                adapter.delete(pos)
            }
        }

    }

    /**
     * Twitter button click listener object. Starts Browser Intent with clicked
     * tweet as URL.
     */
    private val onTwitterBtnClick = object : OnRecycleViewItemClick<TweetWithImage> {
        override fun onItemClick(itemData: TweetWithImage) {
            val uri = Uri.parse("https://twitter.com/${itemData.tweet.username}/status/${itemData.tweet.id}")
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }

    /**
     * Tweet profile click listener object. Navigates to MpInspect.
     */
    private val onProfileClick = object : OnRecycleViewItemClick<TweetWithImage> {
        override fun onItemClick(itemData: TweetWithImage) {
            val nav = findNavController()
            val action = NavActions.fromTwitterFeedToInspect
            action.mpId = itemData.tweet.authorId
            nav.navigate(action)
        }
    }

    /**
     * Tweet delete button click listener object. Deletes clicked item from RecycleView,
     * and updates UI
     */
    private val onDeleteTweetBtnClick = object : OnRecycleViewItemClick<TweetWithImage> {
        override fun onItemClick(itemData: TweetWithImage) {
            lifecycleScope.launch { Repository.twitter.markTweetAsRead(itemData.tweet) }
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