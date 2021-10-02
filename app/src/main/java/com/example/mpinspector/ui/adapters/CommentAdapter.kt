package com.example.mpinspector.ui.adapters

import com.example.mpinspector.R
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.utils.CommentBinding
import java.text.DateFormat
import java.util.*

/**
 * RecycleView Adapter for comments.
 */
class CommentAdapter(items: List<CommentModel>) : GenericAdapter<CommentModel, CommentBinding>(items, R.layout.fragment_comment) {
    override fun hookUpItemWithView(binding: CommentBinding, item: CommentModel) {
        binding.comment.text = item.content
        binding.commentDate.text = DateFormat.getInstance().format(Date(item.timestamp))
        binding.likeIcon.setImageResource(if (item.like) R.drawable.ic_like else R.drawable.ic_dislike)
    }
}
