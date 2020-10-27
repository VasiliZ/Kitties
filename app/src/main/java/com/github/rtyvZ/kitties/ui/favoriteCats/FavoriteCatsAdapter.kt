package com.github.rtyvZ.kitties.ui.favoriteCats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cat_item.view.*

class FavoriteCatsAdapter :
    ListAdapter<FavoriteCatsResponse, FavoriteCatsAdapter.FavoriteCatViewHolder>(
        FavoriteCatsDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCatViewHolder {
        return FavoriteCatViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.favorite_cat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteCatViewHolder, position: Int) {
        val favoriteCat = currentList[position]
        holder.setData(favoriteCat)
    }

    class FavoriteCatViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun setData(favoriteCat: FavoriteCatsResponse?) {
            Glide.with(containerView.imageCat)
                .load(favoriteCat?.catImage?.url)
                .centerCrop()
                .into(containerView.imageCat)
        }
    }
}
