package com.github.rtyvZ.kitties.extentions

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.show() {
    this.visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    this.visibility = View.GONE
}