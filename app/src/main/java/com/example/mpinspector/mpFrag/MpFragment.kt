package com.example.mpinspector.mpFrag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mpinspector.MemberOfParliament
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.utils.PartyMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.util.*

class MpFragment : Fragment() {
    private val logTag = "MP_FRAG"
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_mp, cont, false)
        return binding.root
    }

    override fun onViewCreated(view: View, sInstState: Bundle?) {
        super.onViewCreated(view, sInstState)
        viewModel = ViewModelProvider(this).get(MpViewModel::class.java)
        viewModel.next()

        binding.button.setOnClickListener { viewModel.next() }
        viewModel.currentMp.observe(viewLifecycleOwner, { update(it) })

        lifecycleScope.launch {
            val mps = this.async(Dispatchers.IO) {
                Repository.instance.getMps()
            }
            for (m in mps.await().value!!) {
                Log.d("asd", "${m.first} ${m.last} pn: ${m.personNumber}")
            }
        }
    }

    private fun update(mp: MemberOfParliament) {
        // Try to get party name and icon resources.
        val (iconRes, pNameRes) = runCatching<Pair<Int, Int>> {
            val icon = PartyMapper.partyIcon(mp.party)
            val name = PartyMapper.partyName(mp.party)
            icon to name
        }.fold(onSuccess = { it }, onFailure = {
            Log.e(logTag, "Update fails: ${it.message}")
            return // Abort update. TODO: inform UI about the failure
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