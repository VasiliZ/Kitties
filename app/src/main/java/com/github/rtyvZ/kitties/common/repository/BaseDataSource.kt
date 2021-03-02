package com.github.rtyvZ.kitties.common.repository

import androidx.paging.PagingSource

abstract class BaseDataSource<Key : Any, Value : Any> : PagingSource<Key, Value>() {

    protected var nextKey: Int? = 0

    fun changeKey() {
        nextKey = nextKey?.inc()
    }
}