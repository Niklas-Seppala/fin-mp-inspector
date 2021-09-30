package com.example.mpinspector.ui.mpinspect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentCommentBinding
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.ui.NavActions
import com.example.mpinspector.ui.adapters.ClickableViewHolder
import com.example.mpinspector.ui.adapters.GenericAdapter
import com.example.mpinspector.ui.adapters.ViewHolder
import java.lang.NullPointerException
import java.text.DateFormat
import java.util.*

class CommentAdapter(items: List<CommentModel>, listener: OnMyItemClick)
    : GenericAdapter<CommentModel, FragmentCommentBinding>(items, listener) {

    override fun onBindViewHolder(holder: ClickableViewHolder, position: Int) {
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