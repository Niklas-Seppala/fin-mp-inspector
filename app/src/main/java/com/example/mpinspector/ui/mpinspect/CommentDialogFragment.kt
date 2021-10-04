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
import com.example.mpinspector.ui.anim.AppAnimations

class CommentDialogFragment: DialogFragment() {
    private lateinit var binding: CommentDialogBinding
    private lateinit var viewModel: CommentDialogViewModel
    private var onSubmitCb: ((s: String, like: Boolean) -> Unit)? = null

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.comment_dialog, cont, false)
        viewModel = ViewModelProvider(this).get(CommentDialogViewModel::class.java)

        viewModel.isDislikeActive.observe(viewLifecycleOwner, { dislike ->
            if (dislike) {
                binding.dislikeButton.startAnimation(AppAnimations.iconClickGrow)
                viewModel.isLikeActive.value?.let { like ->
                    if (like) {
                        binding.likeButton.startAnimation(AppAnimations.iconClickToNormal)
                    }
                }
            }
            else if (viewModel.dislikePrevState) {
                binding.dislikeButton.startAnimation(AppAnimations.iconClickToNormal)
            }
        })

        viewModel.isLikeActive.observe(viewLifecycleOwner, { like ->
            if (like) {
                binding.likeButton.startAnimation(AppAnimations.iconClickGrow)
                viewModel.isDislikeActive.value?.let { dislike ->
                    if (dislike) {
                        binding.dislikeButton.startAnimation(AppAnimations.iconClickToNormal)
                    }
                }
            }
            else if (viewModel.likePrevState) {
                binding.likeButton.startAnimation(AppAnimations.iconClickToNormal)
            }
        })
        
        binding.likeButton.setOnClickListener { viewModel.likeClicked() }
        binding.dislikeButton.setOnClickListener { viewModel.dislikeClicked() }
        binding.cancelButton.setOnClickListener { dialog?.cancel() }
        binding.submitButton.setOnClickListener {
            viewModel.isLikeActive.value?.let {

                if (binding.note.text.isBlank()) {
                    binding.note.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.shake))
                    return@setOnClickListener
                }

                if (!viewModel.oneOfLikeButtonsIsActive()) {
                    binding.likeButton.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.shake_medium))
                    binding.dislikeButton.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.shake_medium))
                    return@setOnClickListener
                }
                onSubmitCb?.invoke(binding.note.text.toString(), it)
            }
            binding.note.text.clear()
            dismiss()
        }

        return binding.root
    }

    fun setOnSubmit(cb: (s: String, like: Boolean) -> Unit) { onSubmitCb = cb }
}