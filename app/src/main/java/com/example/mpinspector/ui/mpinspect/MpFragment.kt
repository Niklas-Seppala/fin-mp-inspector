package com.example.mpinspector.ui.mpinspect

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MemberOfParliamentModel
import com.example.mpinspector.utils.MyTime
import com.example.mpinspector.utils.PartyMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.util.*

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
        binding.button.setOnClickListener {
            commentDialog.show()
            commentEditText.text.clear()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, sInstState: Bundle?) {
        super.onViewCreated(view, sInstState)
        viewModel = ViewModelProvider(this).get(MpViewModel::class.java)

        val atPos = arguments?.getInt("mpIndex")
        lifecycleScope.launch {
            viewModel.load(atPos)
            updateMpCard(viewModel.currentMp.value ?: throw NullPointerException("Mp was null"))
        }
    }

    private fun updateMpCard(mp: MemberOfParliamentModel) {

        binding.mpFragCommentView.layoutManager = LinearLayoutManager(context)
        binding.mpFragCommentView.adapter =
            CommentRecyclerViewAdapter(viewModel.comments.value ?: return)   // FAIL POINT

        binding.mpFragNameTv.text = getString(R.string.mpFragFullName, mp.first, mp.last)
        binding.mpFragMinisterTv.text = if (mp.minister) getString( R.string.mpFragIsMinister) else ""
        binding.mpFragConstTv.text = mp.constituency
        binding.mpFragAgeTv.text = getString(R.string.mpFragAge, viewModel.mpAge)

        binding.mpFragPartyIv.setImageResource(viewModel.partyIcon)
        binding.mpFragProfileIv.setImageBitmap(viewModel.image)
        binding.progressBar.visibility = View.GONE
        binding.card.visibility = View.VISIBLE
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
}