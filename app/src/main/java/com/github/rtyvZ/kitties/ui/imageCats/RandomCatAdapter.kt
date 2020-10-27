package com.github.rtyvZ.kitties.ui.imageCats

import RandomCatsDiffCallback
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.extentions.hide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cat_item.*

class RandomCatAdapter(private val setLike: (Cat, StateCatVote) -> Unit) :
    ListAdapter<Cat, RandomCatAdapter.RandomCatViewHolder>(RandomCatsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomCatViewHolder {

        return RandomCatViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.cat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RandomCatViewHolder, position: Int) {
        val cat = currentList[position]
        holder.setData(cat, setLike)
    }

    class RandomCatViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun setData(cat: Cat, setLike: (Cat, StateCatVote) -> Unit) {
            if (cat.choice == 1) {
                thumb_up.background = ResourcesCompat.getDrawable(
                    containerView.context.resources,
                    R.drawable.thumb_up_green,
                    null
                )
            } else {
                thumb_up.background = ResourcesCompat.getDrawable(
                    containerView.context.resources,
                    R.drawable.ic_baseline_thumb_without_vote,
                    null
                )
            }

            if (cat.choice == 0) {
                thumb_down.background = ResourcesCompat.getDrawable(
                    containerView.context.resources,
                    R.drawable.thumb_down_64,
                    null
                )
            } else {
                thumb_down.background = ResourcesCompat.getDrawable(
                    containerView.context.resources,
                    R.drawable.ic_baseline_thumb_down_without_vote,
                    null
                )
            }

            thumb_up.setOnClickListener {
                setLike(cat, StateCatVote.LIKE)
            }

            thumb_down.setOnClickListener {
                setLike(cat, StateCatVote.DISLIKE)
            }

            Glide
                .with(imageCat.context)
                .load(cat.url)
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loadItemProgress.hide()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loadItemProgress.hide()
                        return false
                    }

                })
                .into(imageCat)
        }
    }
}