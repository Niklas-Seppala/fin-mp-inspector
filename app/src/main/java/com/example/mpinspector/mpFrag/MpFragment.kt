package com.example.mpinspector.mpFrag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.repository.Repository
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

        if (arguments != null) {
            val atPos = arguments?.get("mpIndex") as Int
            viewModel.load(atPos)
        }
        viewModel.currentMp.observe(viewLifecycleOwner, { update(it) })
    }

    private fun update(mp: MemberOfParliamentModel) {

        val (iconRes, pNameRes) = runCatching<Pair<Int, Int>> {
            PartyMapper.partyIcon(mp.party) to PartyMapper.partyName(mp.party)
        }.fold(onSuccess = { it }, onFailure = {
            return
        })

        binding.mpFragNameTv.text = getString(R.string.mpFragFullName, mp.first, mp.last)
        binding.mpFragMinisterTv.text = if (mp.minister) getString( R.string.mpFragIsMinister) else ""
        binding.mpFragConstTv.text = mp.constituency
        binding.mpFragSeatTv.text = getString(R.string.mpFragSeatNum, mp.seatNumber)
        binding.mpFragAgeTv.text = getString(R.string.mpFragAge, (year - mp.bornYear))

        binding.mpFragPartyIv.setImageResource(iconRes)
        binding.mpFragPartyName.text = getString(pNameRes)
    }

    private companion object {
        private val year = Calendar.getInstance().get(Calendar.YEAR)
    }
}