package com.example.mpinspector.ui.mpinspect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.ui.adapters.GenericAdapter
import com.example.mpinspector.ui.anim.AppAnimations
import java.lang.RuntimeException

class MpFragment : Fragment(), GenericAdapter.OnMyItemClick {
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel
    private lateinit var commentDialog: CommentDialogFragment

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_mp, cont, false)
        commentDialog = CommentDialogFragment()
        commentDialog.setOnSubmit { if (it.isNotBlank()) viewModel.commentOkButtonClick(it) }

        binding.noteButton.setOnClickListener { noteBtnClick(it) }
        binding.favButton.setOnClickListener { favoriteBtnClick(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, sInstState: Bundle?) {
        super.onViewCreated(view, sInstState)
        val mpId = arguments?.getInt("mpId") ?: throw RuntimeException()
        viewModel = ViewModelProvider(this, MpViewModelFactory(mpId)).get(MpViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.commentsLiveData.observe(viewLifecycleOwner, { updateCommentsView(it) })
        viewModel.mpLiveData.observe(viewLifecycleOwner, { viewModel.mpLoaded = true })
        viewModel.imageLiveData.observe(viewLifecycleOwner, { viewModel.imageLoaded = true})
        viewModel.loadComplete.observe(viewLifecycleOwner, { if (it) updateLoadViews() })
        viewModel.favoriteToast.observe(viewLifecycleOwner, { updateFavoriteToast(it) })
    }

    private fun updateCommentsView(comments: List<CommentModel>) {
        if (binding.mpFragCommentView.adapter != null) {
            val adapter = binding.mpFragCommentView.adapter as CommentAdapter
            adapter.updateItems(comments)
        } else {
            binding.mpFragCommentView.adapter = CommentAdapter(comments, this)
        }
    }

    private fun updateLoadViews() {
        binding.progressBar.visibility = View.GONE
        binding.card.visibility = View.VISIBLE
    }

    private fun updateFavoriteToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun favoriteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        viewModel.favoriteButtonClick()
    }

    private fun noteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        commentDialog.show(childFragmentManager, "")
    }

    override fun onItemClick(pos: Int) {
        TODO("Not yet implemented")
    }
}