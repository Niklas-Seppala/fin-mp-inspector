package com.example.mpinspector

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.mpinspector.databinding.FragmentCommentBinding
import com.example.mpinspector.repository.models.CommentModel

class CommentRecyclerViewAdapter(private val items: List<CommentModel>)
    : RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = FragmentCommentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.comment.text = item.content
        holder.root.setBackgroundColor(if (position % 2 == 0 ) 0xffffffff.toInt() else 0xffeeeeee.toInt())
    }

    inner class ViewHolder(binding: FragmentCommentBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val comment: TextView = binding.contentTv
        val root = binding.root
    }

}