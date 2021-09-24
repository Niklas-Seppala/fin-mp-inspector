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
import kotlinx.coroutines.launch
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

        val atPos = arguments?.getInt("mpIndex") ?: -1 // Crash and burn
        viewModel.load(atPos)


        viewModel.currentMp.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                updateMpCard(it)
            }
        })

        viewModel.currentMp.observe(viewLifecycleOwner, {
            val adapter = binding.mpFragCommentView.adapter ?: return@observe
            adapter.notifyItemChanged(adapter.itemCount-1)
        })
    }

    private suspend fun updateMpCard(mp: MemberOfParliamentModel) {
        val (img, iconRes) = acquireRes(mp) ?: return                              // FAIL POINT

        binding.mpFragCommentView.layoutManager = LinearLayoutManager(context)

        binding.mpFragCommentView.adapter =
            CommentRecyclerViewAdapter(viewModel.comments ?: return)        // FAIL POINT

        binding.mpFragNameTv.text = getString(R.string.mpFragFullName, mp.first, mp.last)
        binding.mpFragMinisterTv.text = if (mp.minister) getString( R.string.mpFragIsMinister) else ""
        binding.mpFragConstTv.text = mp.constituency
        binding.mpFragAgeTv.text = getString(R.string.mpFragAge, viewModel.mpAge)

        binding.mpFragPartyIv.setImageResource(iconRes)
        binding.mpFragProfileIv.setImageBitmap(img)
        binding.progressBar.visibility = View.GONE
        binding.card.visibility = View.VISIBLE
    }

    private suspend fun acquireRes(mp: MemberOfParliamentModel): Pair<Bitmap, Int>? {
        val img = Repository.instance.getImage(mp.personNumber)
        val iconRes = runCatching<Int> {
            PartyMapper.partyIcon(mp.party)
        }.fold(onSuccess = { it }, onFailure = {
            return null
        })
        return img to iconRes
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