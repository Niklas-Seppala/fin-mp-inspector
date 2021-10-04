package com.example.mpinspector.ui.twitter

import android.graphics.Bitmap
import androidx.core.text.HtmlCompat
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentTweetBinding
import com.example.mpinspector.ui.adapters.GenericAdapter
import com.example.mpinspector.ui.adapters.GenericViewHolder
import com.example.mpinspector.ui.adapters.OnRecycleViewItemClick
import com.example.mpinspector.ui.anim.AppAnimations
import java.text.DateFormat
import java.time.Instant


class TweetAdapter(items: List<TweetWithAuthor>,
                   private val images: Map<Int, Bitmap>,
                   otherListeners: Array<OnRecycleViewItemClick<TweetWithAuthor>>) :
    GenericAdapter<TweetWithAuthor, FragmentTweetBinding>(
        items,
        R.layout.fragment_tweet,
        otherListeners = otherListeners
    ) {

    override fun bindAdditionalListeners(
        otherListeners: Array<OnRecycleViewItemClick<TweetWithAuthor>>?,
        viewHolder: GenericViewHolder<TweetWithAuthor, FragmentTweetBinding>
    ) {
        if (otherListeners == null) return

        val openInTwitter = otherListeners[TwitterFeedFragment.OPEN_IN_TWITTER_LISTENER]
        val deleteTweet = otherListeners[TwitterFeedFragment.DELETE_TWEET_LISTENER]

        viewHolder.binding.tweetOpen.setOnClickListener {
            openInTwitter.onItemClick(viewHolder.itemAtCurrentPos())
            viewHolder.binding.tweetOpen.startAnimation(AppAnimations.iconClickAnimation)
        }

        viewHolder.binding.tweetClose.setOnClickListener {
            deleteTweet.onItemClick(viewHolder.itemAtCurrentPos())
            viewHolder.binding.tweetClose.startAnimation(AppAnimations.iconClickAnimation)
        }
    }

    // https://stackoverflow.com/questions/38506598/regular-expression-to-match-hashtag-but-not-hashtag-with-semicolon
    private val hashtagRegex = Regex("\\B((\\#|\\@)[a-zA-Z]+\\b)")

    override fun hookUpItemWithView(
        binding: FragmentTweetBinding,
        item: TweetWithAuthor
    ) {
        val author = item.first
        val tweet = item.second
        val img = images[author.personNumber]

        val colored = tweet.content.replace(hashtagRegex) { "<font color=#004143><b>${it.value}</b></font>" }
        binding.tweetContent.text = HtmlCompat.fromHtml(colored, HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.createdAt.text = DateFormat.getInstance()
            .format(Instant.parse(tweet.createdAt).epochSecond * 1000)
        binding.tweetAuthor.text =
            binding.root.context.getString(R.string.mpFragFullName, author.first, author.last)
        binding.tweetProfilePic.setImageBitmap(img)
    }
}
