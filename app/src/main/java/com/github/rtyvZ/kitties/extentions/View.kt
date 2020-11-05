package com.github.rtyvZ.kitties.extentions

import android.view.View

fun View.toggleVisibility() {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
}