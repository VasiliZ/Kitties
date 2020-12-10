package com.github.rtyvZ.kitties.ui.breedCats

import androidx.recyclerview.widget.DiffUtil

class BreedCatsDiffCallback : DiffUtil.ItemCallback<BreedCats>() {
    override fun areItemsTheSame(oldItem: BreedCats, newItem: BreedCats): Boolean {
        return oldItem.imageId == newItem.imageId
    }

    override fun areContentsTheSame(oldItem: BreedCats, newItem: BreedCats) = oldItem == newItem

}
