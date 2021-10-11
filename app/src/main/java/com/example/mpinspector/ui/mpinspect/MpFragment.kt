package com.example.mpinspector.ui.mpinspect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentMpBinding
import com.example.mpinspector.ui.adapters.CommentAdapter
import com.example.mpinspector.utils.PartyMapper
import com.example.mpinspector.utils.Toaster
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Mp inspect view Fragment class.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class MpFragment : Fragment() {
    private lateinit var binding: FragmentMpBinding
    private lateinit var viewModel: MpViewModel
    private lateinit var noteDialog: NoteDialogFragment
    private lateinit var adapter: CommentAdapter

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_mp, cont, false)

        // Set button listeners.
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

        // Get the mpId from safe args and build view model.
        val mpId = arguments?.getInt("mpId") ?: throw RuntimeException()
        viewModel = ViewModelProvider(this, MpViewModelFactory(mpId)).get(MpViewModel::class.java)


        noteDialog = NoteDialogFragment { text, like ->
            viewModel.submitComment(text, like)
        }

        // Set progress bar active if mp is not fetched in 100ms
        lifecycleScope.launch {
            delay(100)
            viewModel.loadComplete.value?.let {
                if (!it) binding.progressBar.visibility = View.VISIBLE
            }
        }

        createNoteAdapter()
        observeMpAndComments()
        observeTwitterButton()
        observeFavoriteButton()

        viewModel.toastMessage.observe(viewLifecycleOwner, {
            Toaster.make(context, it)
        })

        viewModel.loadComplete.observe(viewLifecycleOwner, {
            if (it) binding.card.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun createNoteAdapter() {
        adapter = CommentAdapter(listOf())
        binding.mpFragCommentView.adapter = adapter
    }

    private fun observeMpAndComments() {
        viewModel.mpInspectBundle.observe(viewLifecycleOwner, {
            // Update note list data
            adapter.update(it.mpWithComments.comments)

            val mp = it.mpWithComments.mp
            // Update mp related views
            binding.mpFragNameTv.text = mp.fullName
            binding.mpFragAgeTv.text = mp.age.toString()
            binding.mpFragPartyIv.setImageResource(PartyMapper.partyIcon(mp.party))
            binding.mpFragConstTv.text = mp.constituency
            binding.mpFragMinisterTv.text = mp.ministerStr
            binding.mpFragProfileIv.setImageBitmap(it.image)

            viewModel.mpLoaded = true
        })
    }

    private fun observeFavoriteButton() {
        viewModel.isFavLiveData.observe(viewLifecycleOwner, {
            binding.favButton.setImageResource(if (it)
                R.drawable.ic_star
            else R.drawable.ic_star_outline)
        })
    }

    private fun observeTwitterButton() {
        viewModel.twitterLiveData.observe(viewLifecycleOwner, {
            binding.twitterButton.setImageResource(it)
            binding.twitterButton.visibility = if (it == 0) View.GONE else View.VISIBLE
            binding.twitterButton.setImageResource(it)
        })
    }

    private fun favoriteBtnClick(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.icon_click))
        viewModel.favoriteButtonClick()
    }

    private fun noteBtnClick(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.icon_click))
        noteDialog.show(childFragmentManager, "")
    }
}