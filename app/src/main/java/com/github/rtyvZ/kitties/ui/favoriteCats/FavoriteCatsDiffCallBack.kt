package com.github.rtyvZ.kitties.ui.favoriteCats

import androidx.recyclerview.widget.DiffUtil
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse

class FavoriteCatsDiffCallBack : DiffUtil.ItemCallback<FavoriteCatsResponse>() {
    override fun areItemsTheSame(
        oldItem: FavoriteCatsResponse,
        newItem: FavoriteCatsResponse
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: FavoriteCatsResponse,
        newItem: FavoriteCatsResponse
    ): Boolean {
        return oldItem.id == newItem.id
    }
}
