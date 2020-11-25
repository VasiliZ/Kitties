package com.github.rtyvZ.kitties.extentions

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.show() {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}

fun ProgressBar.hide() {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    }
}