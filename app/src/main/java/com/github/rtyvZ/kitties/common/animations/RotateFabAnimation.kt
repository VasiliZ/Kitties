package com.github.rtyvZ.kitties.common.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

object RotateFabAnimation {
    fun rotateFab(view: View, rotate: Boolean): Boolean {
        view.animate().setDuration(200).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                super.onAnimationEnd(animation, isReverse)
            }
        }).rotation(if (rotate) 135f else 0f)
        return rotate
    }

    fun showIn(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.translationY = view.height.toFloat()
        view.animate().setDuration(200)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                    super.onAnimationEnd(animation, isReverse)
                }
            }).alpha(1f)
            .start()
    }

    fun showOut(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 1f
        view.translationY = 0f
        view.animate().setDuration(200)
            .translationY(view.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                    super.onAnimationEnd(animation, isReverse)
                }
            }).alpha(0f)
            .start()
    }

    fun init(view: View) {
        view.visibility = View.GONE
        view.translationY = view.height.toFloat()
        view.alpha = 0f
    }
}