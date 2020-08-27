package com.github.rtyvZ.kitties.common

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class LinearManagerWithoutScroll(context: Context) : LinearLayoutManager(context) {

    override fun canScrollVertically(): Boolean {
        return false
    }
}