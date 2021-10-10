package com.example.mpinspector.ui.twitter

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.core.text.HtmlCompat
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentTweetBinding
import com.example.mpinspector.ui.adapters.*
import com.example.mpinspector.utils.MyTime
import com.example.mpinspector.utils.PartyMapper

/**
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
        if (otherListeners == null) return

        val openInTwitter = otherListeners[TwitterFeedFragment.OPEN_IN_TWITTER_LISTENER]
        val moveToInspect = otherListeners[TwitterFeedFragment.INSPECT_PROFILE_LISTENER]

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

    // https://stackoverflow.com/questions/38506598/regular-expression-to-match-hashtag-but-not-hashtag-with-semicolon
    private val hashtagRegex = Regex("\\B((\\#|\\@)[a-zA-Z]+\\b)")

    override fun bindDataToView(binding: FragmentTweetBinding, item: TweetBundle) {
        val colored = item.tweet.content.replace(hashtagRegex) { "<font color=#004143><b>${it.value}</b></font>" }
        binding.tweetContent.text = HtmlCompat.fromHtml(colored, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.TweetPartyIcon.setImageResource(PartyMapper.partyIcon(item.tweet.authorParty))

        binding.createdAt.text = MyTime.strTime(item.tweet.timestamp * 1000)
        binding.tweetAuthor.text = item.tweet.authorName
        binding.tweetProfilePic.setImageBitmap(item.image)
    }
}
