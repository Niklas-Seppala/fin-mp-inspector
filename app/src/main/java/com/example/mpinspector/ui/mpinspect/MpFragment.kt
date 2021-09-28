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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.utils.MyTime
import kotlinx.coroutines.launch
import java.util.*
import com.example.mpinspector.ui.anim.AppAnimations


class MpFragment : Fragment() {
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel
    private lateinit var commentDialog: AlertDialog
    private lateinit var commentEditText: EditText

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_mp, cont, false)

        val dialogLayout = layoutInflater.inflate(R.layout.comment_dialog, cont, false)
        commentDialog = createCommentDialog(dialogLayout)
        commentEditText = dialogLayout.findViewById(R.id.commentEt)

        binding.noteButton.setOnClickListener { noteBtnClick(it) }
        binding.favButton.setOnClickListener { favoriteBtnClick(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, sInstState: Bundle?) {
        super.onViewCreated(view, sInstState)
        viewModel = ViewModelProvider(this).get(MpViewModel::class.java)
        val mpId = arguments?.getInt("mpId")

        viewModel.currentMp.observe(viewLifecycleOwner, { setMpCard(it) })
        viewModel.favBtnImg.observe(viewLifecycleOwner, { updateFavButton(it) })

        viewModel.load(mpId)
    }

    private fun setMpCard(mp: MpModel) {
        binding.mpFragCommentView.layoutManager = LinearLayoutManager(context)

        viewModel.comments.value?.let {
            binding.mpFragCommentView.adapter =
                CommentRecyclerViewAdapter(viewModel.comments.value ?: return)   // FAIL POINT
        }

        binding.mpFragNameTv.text = getString(R.string.mpFragFullName, mp.first, mp.last)
        binding.mpFragMinisterTv.text = if (mp.minister) getString( R.string.mpFragIsMinister) else ""
        binding.mpFragConstTv.text = mp.constituency
        binding.mpFragAgeTv.text = getString(R.string.mpFragAge, viewModel.mpAge)

        binding.mpFragPartyIv.setImageResource(viewModel.partyIcon)
        binding.mpFragProfileIv.setImageBitmap(viewModel.image)
        binding.progressBar.visibility = View.GONE
        binding.card.visibility = View.VISIBLE

        viewModel.favBtnImg.value?.let {
            updateFavButton(it)
        }
    }

    private fun createCommentDialog(view: View) : AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setNegativeButton("Back") { dialog, _ -> dialog.cancel() }
        builder.setPositiveButton("Ok") { _, _ ->
            val mp = viewModel.currentMp.value ?: return@setPositiveButton         // FAIL POINT

            val content = if (commentEditText.text.isNotEmpty())
                commentEditText.text.toString()
            else return@setPositiveButton                                          // FAIL POINT

            val comm = CommentModel(0, mp.personNumber, content, MyTime.timestampLong)
            viewModel.addComment(comm)
        }
        return builder.create()
    }

    private fun updateFavButton(iconRes: Int) {
        binding.favButton.setImageResource(iconRes)
    }

    private fun favoriteBtnClick(view: View) {
        val mpId = viewModel.currentMp.value?.personNumber ?: return
        val name = "${viewModel.currentMp.value?.first} ${viewModel.currentMp.value?.last}"

        lifecycleScope.launch {
            view.startAnimation(AppAnimations.iconClickAnimation)
            val favModel = FavoriteModel(mpId, MyTime.timestampLong)
            val toastMsg = if (viewModel.isFavorite) {
                Repository.mps.removeFavMp(favModel)
                "$name removed from favorites."
            } else {
                Repository.mps.addFavMp(favModel)
                "$name added to favorites."
            }
            viewModel.favBtnPressed()
            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun noteBtnClick(view: View) {
        view.startAnimation(AppAnimations.iconClickAnimation)
        commentDialog.show()
        commentEditText.text.clear()
    }
}