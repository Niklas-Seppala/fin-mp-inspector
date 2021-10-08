package com.example.mpinspector.ui.twitter

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.core.text.HtmlCompat
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentTweetBinding
import com.example.mpinspector.ui.adapters.*
import com.example.mpinspector.utils.PartyMapper
import java.text.DateFormat

class TweetAdapter(private val context: Context,
                   items: List<TweetWithImage>,
                   otherListeners: Array<OnRecycleViewItemClick<TweetWithImage>>) :
    GenericAdapter<TweetWithImage, FragmentTweetBinding>(
        items,
        R.layout.fragment_tweet,
        false,
        otherListeners = otherListeners
    ) {

    override val diffComparator: DiffComparator<TweetWithImage> = { a, b -> a.tweet.id  == b.tweet.id }

    override fun bindAdditionalListeners(
        otherListeners: Array<OnRecycleViewItemClick<TweetWithImage>>?,
        viewHolder: GenericViewHolder<TweetWithImage, FragmentTweetBinding>
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

    override fun hookUpItemWithView(binding: FragmentTweetBinding, item: TweetWithImage) {

        val colored = item.tweet.content.replace(hashtagRegex) { "<font color=#004143><b>${it.value}</b></font>" }
        binding.tweetContent.text = HtmlCompat.fromHtml(colored, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.TweetPartyIcon.setImageResource(PartyMapper.partyIcon(item.tweet.authorParty))

        binding.createdAt.text = DateFormat.getInstance()
            .format(item.tweet.timestamp * 1000L)
        binding.tweetAuthor.text = item.tweet.authorName
        binding.tweetProfilePic.setImageBitmap(item.image)
    }
}
