package com.github.rtyvZ.kitties.common

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class LinearManagerWithoutScroll(context: Context) : LinearLayoutManager(context) {
    private var isScrollEnabled = true


    fun setScrollEnabled(flag: Boolean) {
        this.isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
       return false
    }
}