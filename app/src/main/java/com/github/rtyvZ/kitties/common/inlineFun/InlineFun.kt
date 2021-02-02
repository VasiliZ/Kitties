package com.github.rtyvZ.kitties.common.inlineFun

import androidx.lifecycle.MutableLiveData
import java.lang.Exception

inline fun <A, B, C : MutableLiveData<Throwable>, R> checkNull(
    a: A,
    b: B,
    c: C,
    block: (A, B) -> R
) {
    if (a != null && b != null) {
        block(a, b)
    } else {
        c.postValue(Exception("Value is null"))
    }
}
