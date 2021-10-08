package com.example.mpinspector.ui.mpinspect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.CommentDialogBinding

class CommentDialogFragment: DialogFragment() {
    private lateinit var binding: CommentDialogBinding
    private lateinit var viewModel: CommentDialogViewModel
    private var onSubmitCb: ((s: String, like: Boolean) -> Unit)? = null

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.comment_dialog, cont, false)
        viewModel = ViewModelProvider(this).get(CommentDialogViewModel::class.java)

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

    private fun loadGrowAnim() = AnimationUtils.loadAnimation(context, R.anim.icon_scale_grow)
    private fun loadNormalAnim() = AnimationUtils.loadAnimation(context, R.anim.icon_scale_normal)

    fun setOnSubmit(cb: (s: String, like: Boolean) -> Unit) { onSubmitCb = cb }
}