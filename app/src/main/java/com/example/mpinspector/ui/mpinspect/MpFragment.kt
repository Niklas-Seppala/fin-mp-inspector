package com.example.mpinspector.ui.mpinspect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.ui.adapters.CommentAdapter
import com.example.mpinspector.utils.PartyMapper
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
            it.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.icon_click))
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


        viewModel.mpInspectBundle.observe(viewLifecycleOwner, {
            adapter.update(it.mpWithComments.comments)

            val mp = it.mpWithComments.mp
            val image = it.image
            binding.mpFragNameTv.text = mp.fullName
            binding.mpFragAgeTv.text = mp.age.toString()
            binding.mpFragPartyIv.setImageResource(PartyMapper.partyIcon(mp.party))
            binding.mpFragConstTv.text = mp.constituency

            binding.mpFragMinisterTv.text = mp.ministerStr

            binding.mpFragProfileIv.setImageBitmap(image)
            viewModel.mpLoaded = true
        })

        viewModel.twitterLiveData.observe(viewLifecycleOwner, {
            binding.twitterButton.setImageResource(it)
            binding.twitterButton.visibility = if (it == 0) View.GONE else View.VISIBLE
            binding.twitterButton.setImageResource(it)
        })

        viewModel.isFavLiveData.observe(viewLifecycleOwner, {
            binding.favButton.setImageResource(if (it) R.drawable.ic_star else R.drawable.ic_star_outline)
        })

        viewModel.loadComplete.observe(viewLifecycleOwner, { if (it) binding.card.visibility = View.VISIBLE })
        viewModel.toastMessage.observe(viewLifecycleOwner, { Toaster.make(context, it) })
    }

    private fun favoriteBtnClick(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.icon_click))
        viewModel.favoriteButtonClick()
    }

    private fun noteBtnClick(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.icon_click))
        commentDialog.show(childFragmentManager, "")
    }
}