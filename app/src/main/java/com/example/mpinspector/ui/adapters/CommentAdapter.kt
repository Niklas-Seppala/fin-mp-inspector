package com.example.mpinspector.ui.adapters

import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentCommentBinding
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.utils.IconService
import com.example.mpinspector.utils.MyTime

/**
 * RecycleView Adapter for comments.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class CommentAdapter(items: List<CommentModel>) :
    GenericAdapter<CommentModel, FragmentCommentBinding>(items, R.layout.fragment_comment) {

    override fun bindDataToView(binding: FragmentCommentBinding, item: CommentModel) {
        binding.comment.text = item.content
        binding.commentDate.text = MyTime.strTime(item.timestamp)
        binding.likeIcon.setImageResource(IconService.getLikeIconRes(item.like))
    }

    override val diffCompare: ((CommentModel, CommentModel) -> Boolean) = { a, b -> a.id == b.id }
}
