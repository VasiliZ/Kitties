package com.github.rtyvZ.kitties.ui.catsBreeds

import androidx.recyclerview.widget.DiffUtil
import com.github.rtyvZ.kitties.common.models.CatBreed

class BreedsCatsDiffCallback : DiffUtil.ItemCallback<CatBreed>() {
    override fun areItemsTheSame(oldItem: CatBreed, newItem: CatBreed): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CatBreed, newItem: CatBreed): Boolean {
        return oldItem.id == newItem.id
    }
}
