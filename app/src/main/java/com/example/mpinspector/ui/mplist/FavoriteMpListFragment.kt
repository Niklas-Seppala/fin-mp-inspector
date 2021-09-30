package com.example.mpinspector.ui.mplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentFavMpsBinding

class FavoriteMpListFragment : MpListFragment() {
    private lateinit var viewModel: FavoriteMpListViewModel
    private lateinit var binding: FragmentFavMpsBinding

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_fav_mps, cont, false)
        viewModel = ViewModelProvider(this).get(FavoriteMpListViewModel::class.java)

        viewModel.mps.observe(viewLifecycleOwner, {
            adapter = MpAdapter(it, this)
            binding.list.adapter = adapter
        })
        return binding.root
    }
}