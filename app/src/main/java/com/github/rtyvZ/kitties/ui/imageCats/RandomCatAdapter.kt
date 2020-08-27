package com.github.rtyvZ.kitties.ui.imageCats

import RandomCatsDiffCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.network.data.Cat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cat_item.*

class RandomCatAdapter() :
    ListAdapter<Cat, RandomCatAdapter.RandomCatViewHolder>(RandomCatsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomCatViewHolder =
        RandomCatViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.cat_item, parent, false)
        )

    override fun onBindViewHolder(holder: RandomCatViewHolder, position: Int) {
        val cat = currentList[position]
        holder.setData(cat)
    }

    class RandomCatViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun setData(cat: Cat) {

            Glide
                .with(imageCat.context)
                .load(cat.url)
                .centerCrop()
                .into(imageCat)

        }
    }
}

enum class StateSwipe(value: Int) {
    LIKE(0),
    DISLIKE(1)
}
