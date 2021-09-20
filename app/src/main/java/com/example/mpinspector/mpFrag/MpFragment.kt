package com.example.mpinspector.mpFrag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.MemberOfParliament
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.utils.PartyMapper

class MpFragment : Fragment() {
    private val logTag = "MP_FRAG"
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mp, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MpViewModel::class.java)
        viewModel.next()

        binding.button.setOnClickListener { viewModel.next() }
        viewModel.currentMp.observe(viewLifecycleOwner, { update(it) })
    }

    private fun update(mp: MemberOfParliament) {
        var partyIcon = -1
        var partyName = -1
        val acquireResources = kotlin.runCatching { // TODO: Tidy up
            partyIcon = PartyMapper.partyIcon(mp.party)
            partyName = PartyMapper.partyName(mp.party)
        }

        if (acquireResources.isFailure) {
            Log.e(logTag, "update acquireResources fails") // TODO: inform UI about the failure
            return
        }

        binding.mpFragNameTv.text = getString(R.string.mpFragFullName, mp.first, mp.last)
        binding.mpFragMinisterTv.text = if (mp.minister) getString( R.string.mpFragIsMinister) else ""
        binding.mpFragConstTv.text = mp.constituency
        binding.mpFragSeatTv.text = getString(R.string.mpFragSeatNum, mp.seatNumber)
        binding.mpFragAgeTv.text = getString(R.string.mpFragAge, mp.bornYear)

        binding.mpFragPartyIv.setImageResource(partyIcon)
        binding.mpFragPartyName.text = getString(partyName)
    }
}