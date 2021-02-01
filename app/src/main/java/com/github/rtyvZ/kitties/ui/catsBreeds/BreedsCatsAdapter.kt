package com.github.rtyvZ.kitties.ui.catsBreeds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.models.CatBreed
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.breed_item.*

class BreedsCatsAdapter(private val itemClick: (CatBreed) -> (Unit)) :
    ListAdapter<CatBreed, BreedsCatsAdapter.BreedsCatsViewHolder>(BreedsCatsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedsCatsViewHolder {
        return BreedsCatsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.breed_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BreedsCatsViewHolder, position: Int) {
        val breed = currentList[position]
        holder.setData(breed, itemClick)
    }

    class BreedsCatsViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun setData(breed: CatBreed, itemClick: (CatBreed) -> Unit) {
            breedTitle.text = breed.name
            Glide.with(breedImage.context)
                .load(breed.image?.url)
                .centerCrop()
                .into(breedImage)
            breedContainer.setOnClickListener {
                itemClick.invoke(breed)
            }
        }
    }
}
