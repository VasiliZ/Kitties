package com.github.rtyvZ.kitties.ui.myUploadedCats

import androidx.recyclerview.widget.DiffUtil
import com.github.rtyvZ.kitties.common.models.Cat

class UploadedCatDiffCallBack : DiffUtil.ItemCallback<Cat>() {
    override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.id != newItem.id
    }
}
