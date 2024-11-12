package io.forus.me.android.presentation.view.component.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

fun View.expandView() {

    visibility = View.INVISIBLE
    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)

            val targetHeight = height
            layoutParams.height = 0
            visibility = View.VISIBLE
            requestLayout()


            val animator = ValueAnimator.ofInt(0, targetHeight)
            animator.addUpdateListener { animation ->
                layoutParams.height = animation.animatedValue as Int
                requestLayout()
            }
            animator.duration = 300
            animator.start()

        }
    })
}


fun  View.collapseView() {
    val initialHeight = measuredHeight

    val animator = ValueAnimator.ofInt(initialHeight, 0)
    animator.addUpdateListener { animation ->
        layoutParams.height = animation.animatedValue as Int
        requestLayout()
    }
    animator.duration = 300
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            visibility = View.GONE
        }
    })
    animator.start()
}
