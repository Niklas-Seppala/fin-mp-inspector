package com.example.mpinspector.mpFrag

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mpinspector.CommentRecyclerViewAdapter
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MemberOfParliamentModel
import com.example.mpinspector.utils.PartyMapper
import kotlinx.coroutines.launch
import java.util.*

class MpFragment : Fragment() {
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_mp, cont, false)
        return binding.root
    }

    override fun onViewCreated(view: View, sInstState: Bundle?) {
        super.onViewCreated(view, sInstState)
        viewModel = ViewModelProvider(this).get(MpViewModel::class.java)

        val atPos = arguments?.getInt("mpIndex") ?: -1 // Crash and burn
        viewModel.load(atPos)

        viewModel.currentMp.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                update(it)
            }
        })
    }

    private suspend fun update(mp: MemberOfParliamentModel) {
        val (img, iconRes, pNameRes) = acquireRes(mp) ?: return

        val mockComments = listOf(
            CommentModel(1, "This is a fake comment", 1563216612),
            CommentModel(2, "This is a fake comment", 1563216612),
            CommentModel(3, "This is a fake comment", 1563216612),
            CommentModel(3, "This is a fake comment", 1563216612),
            CommentModel(3, "This is a fake commentThis is a fake commentThis is a fake commentThis is a fake commentThis is a fake commentThis is a fake commentThis is a fake commentThis is a fake commentThis is a fake commentThis is a fake comment", 1563216612),
            CommentModel(3, "This is a fake comment", 1563216612),
            CommentModel(3, "This is a fake comment", 1563216612),
            CommentModel(3, "This is a fake comment", 1563216612),
            CommentModel(3, "This is a fake comment", 1563216612),
            CommentModel(4, "This is a fake comment", 1563216612))
        binding.mpFragCommentView.layoutManager = LinearLayoutManager(context)
        binding.mpFragCommentView.adapter = CommentRecyclerViewAdapter(mockComments)

        binding.mpFragNameTv.text = getString(R.string.mpFragFullName, mp.first, mp.last)
        binding.mpFragMinisterTv.text = if (mp.minister) getString( R.string.mpFragIsMinister) else ""
        binding.mpFragConstTv.text = mp.constituency
        binding.mpFragSeatTv.text = getString(R.string.mpFragSeatNum, mp.seatNumber)
        binding.mpFragAgeTv.text = getString(R.string.mpFragAge, (year - mp.bornYear))

        binding.mpFragPartyIv.setImageResource(iconRes)
        binding.mpFragPartyName.text = getString(pNameRes)
        binding.mpFragProfileIv.setImageBitmap(img)
        binding.progressBar.visibility = View.GONE
    }

    private suspend fun acquireRes(mp: MemberOfParliamentModel): Triple<Bitmap, Int, Int>? {
        val img = Repository.instance.getImage(mp.personNumber)
        val (iconRes, pNameRes) = runCatching<Pair<Int, Int>> {
            PartyMapper.partyIcon(mp.party) to PartyMapper.partyName(mp.party)
        }.fold(onSuccess = { it }, onFailure = {
            return null
        })
        return Triple(img, iconRes, pNameRes)
    }

    private companion object {
        private val year = Calendar.getInstance().get(Calendar.YEAR)
    }
}