package com.example.mpinspector.ui.mpinspect

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.CommentDialogBinding
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.ui.anim.AppAnimations
import com.example.mpinspector.utils.PartyMapper
import java.lang.RuntimeException

class MpFragment : Fragment() {
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel
    private lateinit var commentDialog: CommentDialogFragment
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var dialogBinding: CommentDialogBinding

    private var mpId = -1

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_mp, cont, false)
        dialogBinding = DataBindingUtil.inflate(layoutInflater, R.layout.comment_dialog, cont, false)
        commentDialog = CommentDialogFragment()
        commentDialog.setOnSubmit {
            if (it.isNotBlank()) viewModel.commentOkButtonClick(it)
        }

        binding.noteButton.setOnClickListener { noteBtnClick(it) }
        binding.favButton.setOnClickListener { favoriteBtnClick(it) }

        commentAdapter = CommentAdapter(listOf())
        binding.mpFragCommentView.adapter = commentAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, sInstState: Bundle?) {
        super.onViewCreated(view, sInstState)
        mpId = arguments?.getInt("mpId") ?: throw RuntimeException()
        viewModel = ViewModelProvider(this, MpViewModelFactory(mpId)).get(MpViewModel::class.java)

        viewModel.commentsLiveData.observe(viewLifecycleOwner, { updateCommentsView(it) })
        viewModel.mpLiveData.observe(viewLifecycleOwner, { updateMpViews(it) })
        viewModel.iconLiveData.observe(viewLifecycleOwner, { updateFavoriteButton(it) })
        viewModel.imageLiveData.observe(viewLifecycleOwner, { updateMpProfileView(it) })
        viewModel.loadComplete.observe(viewLifecycleOwner, { if (it) updateLoadViews() })
        viewModel.favoriteToast.observe(viewLifecycleOwner, { updateFavoriteToast(it) })
    }

    private fun updateCommentsView(comments: List<CommentModel>) {
        val adapter = binding.mpFragCommentView.adapter as CommentAdapter
        adapter.setItems(comments)
    }

    private fun updateMpProfileView(image: Bitmap) {
        binding.mpFragProfileIv.setImageBitmap(image)
        viewModel.imageLoaded = true
    }

    private fun updateFavoriteButton(resId: Int) {
        binding.favButton.setImageResource(resId)
    }

    private fun updateLoadViews() {
        binding.progressBar.visibility = View.GONE
        binding.card.visibility = View.VISIBLE
    }

    private fun updateFavoriteToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun updateMpViews(mp: MpModel) {
        binding.mpFragNameTv.text = getString(R.string.mpFragFullName, mp.first, mp.last)
        binding.mpFragMinisterTv.text = if (mp.minister) getString(R.string.mpFragIsMinister) else ""
        binding.mpFragConstTv.text = mp.constituency
        binding.mpFragAgeTv.text = getString(R.string.mpFragAge, viewModel.mpAge)
        binding.mpFragPartyIv.setImageResource(PartyMapper.partyIcon(mp.party))
        viewModel.mpLoaded = true
    }

    private fun favoriteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        viewModel.favoriteButtonClick()
    }

    private fun noteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        commentDialog.show(childFragmentManager, "")
        dialogBinding.commentEt.text.clear()
    }
}