package com.example.mpinspector.ui.adapters

import com.example.mpinspector.R
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.utils.CommentBinding
import java.text.DateFormat
import java.util.*

class CommentAdapter(items: List<CommentModel>) :
    GenericAdapter<CommentModel, CommentBinding, ViewHolder<CommentBinding>>(items, R.layout.fragment_comment) {

    override fun createViewHolder(binding: CommentBinding): ViewHolder<CommentBinding> {
        return ViewHolder(binding)
    }

    override fun bind(binding: CommentBinding, item: CommentModel) {
        binding.contentTv.text = item.content
        binding.commentDateTv.text = DateFormat.getInstance().format(Date(item.timestamp))
    }
}