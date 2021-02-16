package com.github.rtyvZ.kitties.ui.catsBreeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.databinding.BreedItemBinding

class BreedsPagingCatsAdapter(private val itemClick: (CatBreed) -> (Unit)) :
    PagingDataAdapter<CatBreed, BreedsPagingCatsAdapter.BreedsCatsViewHolder>(BreedsCatsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedsCatsViewHolder {
        return BreedsCatsViewHolder(
            BreedItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BreedsCatsViewHolder, position: Int) {
        val breed = getItem(position)
        holder.setData(breed, itemClick)
    }

    class BreedsCatsViewHolder(private val binding: BreedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(breed: CatBreed?, itemClick: (CatBreed) -> Unit) {
            breed?.let {
                binding.breedTitle.text = breed.name
                Glide.with(binding.breedImage.context)
                    .load(breed.image?.url)
                    .centerCrop()
                    .into(binding.breedImage)
                binding.breedContainer.setOnClickListener {
                    itemClick.invoke(breed)
                }
            }
        }
    }
}
