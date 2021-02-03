package com.github.rtyvZ.kitties.ui.myUploadedCats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.databinding.UploadedCatBinding

class UploadedCatAdapter(private val longClick: (cat: Cat) -> Unit) :
    ListAdapter<Cat, UploadedCatAdapter.UploadCatViewHolder>(UploadedCatDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadCatViewHolder {
        return UploadCatViewHolder(
            UploadedCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UploadCatViewHolder, position: Int) {
        holder.setData(currentList[position], longClick)
    }

    class UploadCatViewHolder(private val binding: UploadedCatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(cat: Cat?, longClick: (cat: Cat) -> Unit) {
            Glide.with(binding.uploadedCat.context)
                .load(cat?.url)
                .centerCrop()
                .into(binding.uploadedCat)

            binding.uploadedCat.setOnLongClickListener {
                cat?.let {
                    longClick.invoke(cat)
                }
                true
            }
        }
    }
}
