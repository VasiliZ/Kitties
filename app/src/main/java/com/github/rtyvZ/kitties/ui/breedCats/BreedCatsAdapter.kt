package com.github.rtyvZ.kitties.ui.breedCats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.breed_cat_item.*

class BreedCatsAdapter :
    ListAdapter<BreedCats, BreedCatsAdapter.BreedCatsHolder>(BreedCatsDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedCatsHolder {
        return BreedCatsHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.breed_cat_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BreedCatsHolder, position: Int) {
        holder.setData(currentList[position])
    }


    class BreedCatsHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun setData(breedCats: BreedCats?) {

            Glide.with(image_for_cat_breed.context)
                .load(breedCats?.url)
                .centerCrop()
                .into(image_for_cat_breed)

            nameBreed.text = breedCats?.nameBreed
        }
    }
}
