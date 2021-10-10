package com.example.mpinspector.ui.mpinspect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.CommentDialogBinding

/**
 * Dialog fragment class for user input for creating notes.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class NoteDialogFragment(private val onSubmit: ((String, Boolean) -> Unit)): DialogFragment() {

    private lateinit var binding: CommentDialogBinding
    private lateinit var viewModel: NoteDialogViewModel

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.comment_dialog, cont, false)
        viewModel = ViewModelProvider(this).get(NoteDialogViewModel::class.java)

        observeLike()
        observeDislike()

        binding.likeButton.setOnClickListener { viewModel.likeClicked() }
        binding.dislikeButton.setOnClickListener { viewModel.dislikeClicked() }
        binding.cancelButton.setOnClickListener { dialog?.cancel() }

        binding.submitButton.setOnClickListener {
            if (validate()) {
                // Validation successful.
                viewModel.isLikeActive.value?.let {
                    onSubmit(binding.note.text.toString(), it)
                    binding.note.text.clear()
                    dismiss()
                }
            }
            // Validation failed, dialog will remain active.
        }

        return binding.root
    }

    /**
     * Validates user input fields. Note text input must have non blank
     * text, and like/dislike must be active.
     */
    private fun validate() : Boolean {
        var valResult = false
        viewModel.isLikeActive.value?.let {
            valResult = true

            if (binding.note.text.isBlank()) {
                binding.note.startAnimation(loadShakeAnim())
                valResult = false
            }

            if (!viewModel.oneOfLikeButtonsIsActive()) {
                binding.likeButton.startAnimation(loadShakeAnimMedium())
                binding.dislikeButton.startAnimation(loadShakeAnimMedium())
                valResult = false
            }
        }
        return valResult
    }

    /**
     * Toggle Like and Dislike button animations.
     */
    private fun observeLike() {
        viewModel.isLikeActive.observe(viewLifecycleOwner, { like ->
            if (like) {
                binding.likeButton.startAnimation(loadGrowAnim())
                viewModel.isDislikeActive.value?.let { dislike ->
                    if (dislike)
                        binding.dislikeButton.startAnimation(loadNormalAnim())
                }
            }
            else if (viewModel.likePrevState) {
                binding.likeButton.startAnimation(loadNormalAnim())
            }
        })
    }

    /**
     * Toggle like/dislike buttons.
     */
    private fun observeDislike() {
        viewModel.isDislikeActive.observe(viewLifecycleOwner, { dislike ->
            if (dislike) {
                // Dislike is now active
                binding.dislikeButton.startAnimation(loadGrowAnim())
                // Check if like button needs to shrink.
                viewModel.isLikeActive.value?.let { like ->
                    if (like)
                        binding.likeButton.startAnimation(loadNormalAnim())
                }
            }
            // Dislike is now inactive.
            // Check if it needs to shrink
            else if (viewModel.dislikePrevState) {
                binding.dislikeButton.startAnimation(loadNormalAnim())
            }
        })
    }

    private fun loadGrowAnim() = AnimationUtils.loadAnimation(context, R.anim.icon_scale_grow)
    private fun loadNormalAnim() = AnimationUtils.loadAnimation(context, R.anim.icon_scale_normal)
    private fun loadShakeAnim() = AnimationUtils.loadAnimation(context, R.anim.shake)
    private fun loadShakeAnimMedium() = AnimationUtils.loadAnimation(context, R.anim.shake_medium)
}