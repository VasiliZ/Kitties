package com.github.rtyvZ.kitties.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.databinding.NetworkStateItemLayoutBinding

class NetworkStateViewHolder(
    parent: ViewGroup,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.network_state_item_layout, parent, false
    )
) {
    private val binding = NetworkStateItemLayoutBinding.bind(itemView)
    val progress = binding.progressPaging
    val errorMsg = binding.errorMsg
    val retryButton = binding.retryButton.also {
        it.setOnClickListener {
            retryCallback()
        }
    }

    fun bindTo(loadState: LoadState) {
        progress.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        errorMsg.text = (loadState as? LoadState.Error)?.error?.message
    }
}