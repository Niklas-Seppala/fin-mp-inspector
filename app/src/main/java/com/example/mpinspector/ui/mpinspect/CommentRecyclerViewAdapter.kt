package com.example.mpinspector.ui.mpinspect

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentCommentBinding
import com.example.mpinspector.repository.models.CommentModel
import java.text.DateFormat
import java.util.*

class CommentRecyclerViewAdapter(private val items: List<CommentModel>)
    : RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder>() {

    private var lightColor: Int = 0
    private var darkColor: Int = 0

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = FragmentCommentBinding.inflate(inflater, parent, false)
        lightColor = ContextCompat.getColor(binding.root.context, R.color.comment_dark)
        darkColor = ContextCompat.getColor(binding.root.context, R.color.comment_light)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.comment.text = item.content
        holder.date.text = DateFormat.getInstance().format(Date(item.timestamp))
        holder.root.setBackgroundColor(if (position % 2 == 0 ) darkColor else lightColor)
    }

    inner class ViewHolder(binding: FragmentCommentBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val comment: TextView = binding.contentTv
        val date: TextView = binding.commentDateTv
        val root = binding.root
    }

}