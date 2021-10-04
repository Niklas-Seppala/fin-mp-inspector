package com.example.mpinspector.ui.mpinspect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.ui.adapters.CommentAdapter
import com.example.mpinspector.ui.anim.AppAnimations
import com.example.mpinspector.utils.Toaster

class MpFragment : Fragment() {
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel
    private lateinit var commentDialog: CommentDialogFragment
    private lateinit var adapter: CommentAdapter

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_mp, cont, false)
        commentDialog = CommentDialogFragment()
        commentDialog.setOnSubmit { text, like ->
            viewModel.commentOkButtonClick(text, like)
        }

        binding.noteButton.setOnClickListener { noteBtnClick(it) }
        binding.favButton.setOnClickListener { favoriteBtnClick(it) }
        binding.twitterButton.setOnClickListener {
            it.startAnimation(AppAnimations.iconClickAnimation)
            viewModel.twitterButtonClicked()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, sInstState: Bundle?) {
        super.onViewCreated(view, sInstState)
        val mpId = arguments?.getInt("mpId") ?: throw RuntimeException()
        viewModel = ViewModelProvider(this, MpViewModelFactory(mpId)).get(MpViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = CommentAdapter(listOf())
        binding.mpFragCommentView.adapter = adapter

        viewModel.commentsLiveData.observe(viewLifecycleOwner, { adapter.update(it) })
        viewModel.mpLiveData.observe(viewLifecycleOwner, { viewModel.mpLoaded = true })
        viewModel.imageLiveData.observe(viewLifecycleOwner, { viewModel.imageLoaded = true })
        viewModel.loadComplete.observe(viewLifecycleOwner, { if (it) updateLoadViews() })
        viewModel.toastMessage.observe(viewLifecycleOwner, { updateFavoriteToast(it) })
    }

    private fun updateLoadViews() {
        binding.progressBar.visibility = View.GONE
        binding.card.visibility = View.VISIBLE
    }

    private fun updateFavoriteToast(msg: String) {
        Toaster.make(context, msg)
    }

    private fun favoriteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        viewModel.favoriteButtonClick()
    }

    private fun noteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        commentDialog.show(childFragmentManager, "")
    }
}