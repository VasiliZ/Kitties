package com.github.rtyvZ.kitties.ui.randomCats

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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.databinding.CatItemBinding
import com.github.rtyvZ.kitties.extentions.hide

class RandomCatAdapter(
    private val setLike: (Cat, StateCatVote) -> Unit,
    private val openContextMenu: (cat: Cat, view: View) -> Unit
) :
    ListAdapter<Cat, RandomCatAdapter.RandomCatViewHolder>(RandomCatsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomCatViewHolder {

        return RandomCatViewHolder(
            CatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RandomCatViewHolder, position: Int) {
        val cat = currentList[position]
        holder.setData(cat, setLike, openContextMenu)
    }

    class RandomCatViewHolder(
        val binding: CatItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(
            cat: Cat,
            setLike: (Cat, StateCatVote) -> Unit,
            openContextMenu: (cat: Cat, view: View) -> Unit
        ) {
            binding.imageCat.setOnLongClickListener {
                openContextMenu.invoke(cat, it)
                true
            }
            if (cat.choice == 1) {
                binding.thumbUp.background = ResourcesCompat.getDrawable(
                    binding.root.context.resources,
                    R.drawable.thumb_up_green,
                    null
                )
            } else {
                binding.thumbUp.background = ResourcesCompat.getDrawable(
                    binding.root.context.resources,
                    R.drawable.ic_baseline_thumb_without_vote,
                    null
                )
            }

            if (cat.choice == 0) {
                binding.thumbDown.background = ResourcesCompat.getDrawable(
                    binding.root.context.resources,
                    R.drawable.thumb_down_64,
                    null
                )
            } else {
                binding.thumbDown.background = ResourcesCompat.getDrawable(
                    binding.root.context.resources,
                    R.drawable.ic_baseline_thumb_down_without_vote,
                    null
                )
            }

            binding.thumbUp.setOnClickListener {
                setLike(cat, StateCatVote.LIKE)
            }

            binding.thumbDown.setOnClickListener {
                setLike(cat, StateCatVote.DISLIKE)
            }

            Glide
                .with(binding.imageCat.context)
                .load(cat.url)
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loadItemProgress.hide()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loadItemProgress.hide()
                        return false
                    }

                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageCat)
        }
    }
}