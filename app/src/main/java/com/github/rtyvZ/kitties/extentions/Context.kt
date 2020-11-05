package com.github.rtyvZ.kitties.extentions

import android.content.Context
import com.github.rtyvZ.kitties.common.App

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}

//extension property
val Context.app: App
    get() = applicationContext as App
