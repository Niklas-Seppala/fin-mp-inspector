package com.example.mpinspector.ui.adapters

import android.icu.text.DateFormat
import com.example.mpinspector.R
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.utils.CommentBinding
import java.util.*

object MyDateFormat {
    const val format = "${DateFormat.ABBR_MONTH_DAY} ${DateFormat.HOUR24_MINUTE} ${DateFormat.YEAR}"
}

/**
 * RecycleView Adapter for comments.
 */
class CommentAdapter(items: List<CommentModel>) : GenericAdapter<CommentModel, CommentBinding>(items, R.layout.fragment_comment) {
    override fun hookUpItemWithView(binding: CommentBinding, item: CommentModel) {
        binding.comment.text = item.content
        binding.commentDate.text =
            DateFormat.getPatternInstance(MyDateFormat.format).format(Date(item.timestamp))
        binding.likeIcon.setImageResource(if (item.like) R.drawable.ic_like else R.drawable.ic_dislike)
    }

    override val diffComparator: DiffComparator<CommentModel> = { a, b -> a.id == b.id }
}
