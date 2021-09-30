package com.example.mpinspector.ui.mpinspect

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mpinspector.databinding.FragmentCommentBinding
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.ui.adapters.GenericAdapter
import com.example.mpinspector.ui.adapters.ViewHolder
import java.text.DateFormat
import java.util.*

class CommentAdapter(items: List<CommentModel>) : GenericAdapter<CommentModel, FragmentCommentBinding>(items) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = currentItems[position]
        holder.binding as FragmentCommentBinding
        holder.binding.contentTv.text = item.content
        holder.binding.commentDateTv.text = DateFormat.getInstance().format(Date(item.timestamp))
    }

    override fun createBinding(parent: ViewGroup): FragmentCommentBinding {
        return FragmentCommentBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
    }
}