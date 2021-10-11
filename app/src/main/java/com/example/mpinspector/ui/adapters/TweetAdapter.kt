package com.example.mpinspector.ui.adapters

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.core.text.HtmlCompat
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentTweetBinding
import com.example.mpinspector.ui.twitter.TweetBundle
import com.example.mpinspector.ui.twitter.TwitterFeedFragment
import com.example.mpinspector.utils.MyTime
import com.example.mpinspector.utils.PartyMapper

/**
 * RecycleViewAdapter class for tweet view. Binds an array of click listeners
 * to view components. Processes Tweet content and colors "#" and "@".
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class TweetAdapter(private val context: Context,
                   items: List<TweetBundle>,
                   otherListeners: Array<OnRecycleViewItemClick<TweetBundle>>) :
    GenericAdapter<TweetBundle, FragmentTweetBinding>(
        items,
        R.layout.fragment_tweet,
        false,
        otherListeners = otherListeners
    ) {

    override val diffCompare: (TweetBundle, TweetBundle) -> Boolean = { a, b -> a.tweet.id  == b.tweet.id }

    override fun bindAdditionalListeners(
        otherListeners: Array<OnRecycleViewItemClick<TweetBundle>>?,
        viewHolder: GenericViewHolder<TweetBundle, FragmentTweetBinding>
    ) {
        // Unpack listeners from array.
        if (otherListeners.isNullOrEmpty()) return
        val openInTwitter = otherListeners[TwitterFeedFragment.OPEN_IN_TWITTER_LISTENER]
        val moveToInspect = otherListeners[TwitterFeedFragment.INSPECT_PROFILE_LISTENER]

        // Set listeners.
        viewHolder.binding.tweetOpen.setOnClickListener {
            openInTwitter.onItemClick(viewHolder.itemAtCurrentPos())
            viewHolder.binding.tweetOpen.startAnimation(AnimationUtils.loadAnimation(context, R.anim.icon_click))
        }
        viewHolder.binding.tweetProfilePic.setOnClickListener {
            moveToInspect.onItemClick(viewHolder.itemAtCurrentPos())
        }
        viewHolder.binding.tweetAuthor.setOnClickListener {
            moveToInspect.onItemClick(viewHolder.itemAtCurrentPos())
        }
    }

    override fun bindDataToView(binding: FragmentTweetBinding, item: TweetBundle) {
        // Process Tweet's text content.
        val colored = item.tweet.content.replace(hashtagRegex) { "<font color=#004143><b>${it.value}</b></font>" }
        binding.tweetContent.text = HtmlCompat.fromHtml(colored, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.TweetPartyIcon.setImageResource(PartyMapper.partyIcon(item.tweet.authorParty))

        binding.createdAt.text = MyTime.strTime(item.tweet.timestamp * 1000)
        binding.tweetAuthor.text = item.tweet.authorName
        binding.tweetProfilePic.setImageBitmap(item.image)
    }

    private companion object {
        val hashtagRegex = Regex("\\B((\\#|\\@)[a-zA-Z]+\\b)")
    }
}
