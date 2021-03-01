package com.github.rtyvZ.kitties.common

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.github.rtyvZ.kitties.ui.catsBreeds.BreedsPagingCatsAdapter

class DataLoadsStateAdapter(private val adapter: BreedsPagingCatsAdapter) :
    LoadStateAdapter<NetworkStateViewHolder>() {
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