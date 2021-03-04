package com.github.rtyvZ.kitties.ui.favoriteCats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.databinding.FavoriteCatItemBinding
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse

class FavoriteCatsAdapter() :
    PagingDataAdapter<FavoriteCatsResponse, FavoriteCatsAdapter.FavoriteCatViewHolder>(
        FavoriteCatsDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCatViewHolder {
        return FavoriteCatViewHolder(
            FavoriteCatItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteCatViewHolder, position: Int) {
        val favoriteCat = getItem(position)
        holder.setData(favoriteCat)
    }

    fun getCat(position: Int) = getItem(position)

    class FavoriteCatViewHolder(
        private val binding: FavoriteCatItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setData(favoriteCat: FavoriteCatsResponse?) {
            Glide.with(binding.imageCat)
                .load(favoriteCat?.catImage?.url)
                .centerCrop()
                .into(binding.imageCat)
        }
    }
}
