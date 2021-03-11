package com.github.rtyvZ.kitties.ui.randomCats

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.github.rtyvZ.kitties.common.NetworkStateViewHolder

class DataLoadStateRandomKittiesAdapter(private val adapter: RandomPagingCatAdapter): LoadStateAdapter<NetworkStateViewHolder>() {
    override fun onBindViewHolder(holder: NetworkStateViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateViewHolder {
        return NetworkStateViewHolder(parent) {
            adapter.retry()
        }
    }
}