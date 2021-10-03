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

class TwitterFeedFragment : Fragment() {
    private lateinit var binding: FragmentTwitterFeedBinding
    private lateinit var viewModel: TwitterFeedViewModel

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_twitter_feed, cont, false)
        viewModel = ViewModelProvider(this).get(TwitterFeedViewModel::class.java)


        viewModel.images.observe(viewLifecycleOwner, { imageMap ->
            viewModel.tweets.observe(viewLifecycleOwner, { tweetList ->
                binding.tweetList.adapter = TweetAdapter(tweetList, imageMap)
                binding.twitterFeedSpinner.visibility = View.GONE
            })
        })

        return binding.root
    }
}