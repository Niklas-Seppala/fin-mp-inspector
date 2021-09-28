package com.example.mpinspector.ui.anim

import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import kotlin.math.abs


object AppAnimations {
    val iconClickAnimation: Animation = IconClickAnimation()
}

class IconClickAnimation(fromX: Float = 1f,
                         toX: Float = 1.5f,
                         fromY: Float = 1f,
                         toY: Float = 1.5f,
                         pivotXType: Int = Animation.RELATIVE_TO_SELF,
                         pivotX: Float = 0.5f,
                         pivotY: Float = 0.5f,) : ScaleAnimation(fromX, toX, fromY, toY,
                                                                  pivotXType, pivotX, pivotXType,
                                                                  pivotY) {
    init {
        setInterpolator { abs(it - 1f) }
        duration = 200 //MS
    }
}