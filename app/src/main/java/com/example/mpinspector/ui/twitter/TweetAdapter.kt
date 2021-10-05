package com.example.mpinspector.ui.twitter

import android.graphics.Bitmap
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentTweetBinding
import com.example.mpinspector.ui.adapters.*
import com.example.mpinspector.ui.anim.AppAnimations
import com.example.mpinspector.utils.PartyMapper
import java.text.DateFormat
import java.time.Instant


class TweetAdapter(items: List<TweetWithAuthor>,
                   private var images: Map<Int, Bitmap>,
                   otherListeners: Array<OnRecycleViewItemClick<TweetWithAuthor>>) :
    GenericAdapter<TweetWithAuthor, FragmentTweetBinding>(
        items,
        R.layout.fragment_tweet,
        otherListeners = otherListeners
    ) {

    override val diffComparator: DiffComparator<TweetWithAuthor> = { a, b -> a.second.id  == b.second.id }

    fun updateWithImages(newItems: List<TweetWithAuthor>, newImages: Map<Int, Bitmap>) {
        images = newImages
        update(newItems)
    }

    override fun bindAdditionalListeners(
        otherListeners: Array<OnRecycleViewItemClick<TweetWithAuthor>>?,
        viewHolder: GenericViewHolder<TweetWithAuthor, FragmentTweetBinding>
    ) {
        if (otherListeners == null) return

        val openInTwitter = otherListeners[TwitterFeedFragment.OPEN_IN_TWITTER_LISTENER]
        val deleteTweet = otherListeners[TwitterFeedFragment.DELETE_TWEET_LISTENER]
        val moveToInspect = otherListeners[TwitterFeedFragment.INSPECT_PROFILE_LISTENER]

        viewHolder.binding.tweetOpen.setOnClickListener {
            openInTwitter.onItemClick(viewHolder.itemAtCurrentPos())
            viewHolder.binding.tweetOpen.startAnimation(AppAnimations.iconClickAnimation)
        }

        viewHolder.binding.tweetClose.setOnClickListener {
            deleteTweet.onItemClick(viewHolder.itemAtCurrentPos())
            viewHolder.binding.tweetClose.startAnimation(AppAnimations.iconClickAnimation)
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

    override fun hookUpItemWithView(
        binding: FragmentTweetBinding,
        item: TweetWithAuthor
    ) {
        val author = item.first
        val tweet = item.second
        val img = images[author.personNumber]

        val colored = tweet.content.replace(hashtagRegex) { "<font color=#004143><b>${it.value}</b></font>" }
        binding.tweetContent.text = HtmlCompat.fromHtml(colored, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.TweetPartyIcon.setImageResource(PartyMapper.partyIcon(author.party))

        binding.createdAt.text = DateFormat.getInstance()
            .format(Instant.parse(tweet.createdAt).epochSecond * 1000)
        binding.tweetAuthor.text =
            binding.root.context.getString(R.string.mpFragFullName, author.first, author.last)
        binding.tweetProfilePic.setImageBitmap(img)
    }
}
