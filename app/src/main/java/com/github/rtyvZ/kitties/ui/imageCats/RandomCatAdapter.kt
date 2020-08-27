package com.github.rtyvZ.kitties.ui.imageCats

import RandomCatsDiffCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.network.data.Cat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cat_item.*

class RandomCatAdapter(private val swipeCallback: (Int, StateSwipe) -> Unit) :
    ListAdapter<Cat, RandomCatAdapter.RandomCatViewHolder>(RandomCatsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomCatViewHolder =
        RandomCatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cat_item, parent, false)
        )

    override fun onBindViewHolder(holder: RandomCatViewHolder, position: Int) {
        val cat = currentList[position]
        holder.setData(cat, swipeCallback)
    }

    class RandomCatViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun setData(cat: Cat, swipeCallback: (Int, StateSwipe) -> Unit) {

            Glide
                .with(imageCat.context)
                .load(cat.url)
                .centerCrop()
                .into(imageCat)

            motionContainer.setTransitionListener(object : TransitionAdapter() {
                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    when (currentId) {
                        R.id.likeSet -> {
                            swipeCallback(adapterPosition, StateSwipe.LIKE)
                        }

                        R.id.dislikeSet -> {
                            swipeCallback(adapterPosition, StateSwipe.DISLIKE)
                        }
                    }
                }
            })
        }
    }
}

enum class StateSwipe(value: Int) {
    LIKE(0),
    DISLIKE(1)
}
