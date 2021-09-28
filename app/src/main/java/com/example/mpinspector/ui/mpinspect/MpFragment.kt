package com.example.mpinspector.ui.mpinspect

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.utils.MyTime
import java.util.*
import com.example.mpinspector.ui.anim.AppAnimations
import com.example.mpinspector.utils.PartyMapper
import java.lang.RuntimeException

class MpFragment : Fragment() {
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel
    private lateinit var commentDialog: AlertDialog
    private lateinit var commentEditText: EditText

    private var mpId = -1

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_mp, cont, false)

        val dialogLayout = layoutInflater.inflate(R.layout.comment_dialog, cont, false)
        commentDialog = createCommentDialog(dialogLayout)
        commentEditText = dialogLayout.findViewById(R.id.commentEt)

        binding.noteButton.setOnClickListener { noteBtnClick(it) }
        binding.favButton.setOnClickListener { favoriteBtnClick(it) }

        binding.mpFragCommentView.adapter = CommentAdapter(listOf())

        return binding.root
    }

    override fun onViewCreated(view: View, sInstState: Bundle?) {
        super.onViewCreated(view, sInstState)
        mpId = arguments?.getInt("mpId") ?: throw RuntimeException()

        viewModel = ViewModelProvider(this, MpViewModelFactory(mpId)).get(MpViewModel::class.java)

        viewModel.comments.observe(viewLifecycleOwner, {
            (binding.mpFragCommentView.adapter as CommentAdapter).setItems(it)
        })

        viewModel.mp.observe(viewLifecycleOwner, {
            binding.mpFragNameTv.text = getString(R.string.mpFragFullName, it.first, it.last)
            binding.mpFragMinisterTv.text = if (it.minister) getString(R.string.mpFragIsMinister) else ""
            binding.mpFragConstTv.text = it.constituency
            binding.mpFragAgeTv.text = getString(R.string.mpFragAge, viewModel.mpAge)
            binding.mpFragPartyIv.setImageResource(PartyMapper.partyIcon(it.party))
            viewModel.mpLoaded = true
        })

        viewModel.image.observe(viewLifecycleOwner, {
            binding.mpFragProfileIv.setImageBitmap(it)
            viewModel.imageLoaded = true
        })

        viewModel.loadComplete.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.card.visibility = View.VISIBLE
            }
        })

        viewModel.favoriteButtonImage.observe(viewLifecycleOwner, {
            binding.favButton.setImageResource(it)
        })
    }

    private fun createCommentDialog(view: View) : AlertDialog {
        return AlertDialog.Builder(context)
            .setView(view)
            .setNegativeButton("Back") { dialog, _ -> dialog.cancel() }
            .setPositiveButton("Ok") { _, _ ->
                val mp = viewModel.mp.value ?: return@setPositiveButton
                val content = if (commentEditText.text.isNotEmpty())
                    commentEditText.text.toString()
                else return@setPositiveButton
                val comm = CommentModel(0, mp.personNumber, content, MyTime.timestampLong)
                viewModel.addComment(comm)
            }.create()
    }

    private fun favoriteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        val toastMsg =
            if (viewModel.isFavorite) "${viewModel.fullName} removed from favorites."
            else "${viewModel.fullName} added to favorites."
        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
        viewModel.favoriteButtonClick()
    }

    private fun noteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        commentDialog.show()
        commentEditText.text.clear()
    }
}