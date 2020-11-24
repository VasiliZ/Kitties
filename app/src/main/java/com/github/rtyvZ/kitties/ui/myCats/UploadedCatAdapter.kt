package com.github.rtyvZ.kitties.ui.myCats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.models.Cat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.uploaded_cat.*

class UploadedCatAdapter(private val longClick: (idImage: String) -> Unit) :
    ListAdapter<Cat, UploadedCatAdapter.UploadCatViewHolder>(UploadedCatDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadCatViewHolder {
        return UploadCatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.uploaded_cat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UploadCatViewHolder, position: Int) {
        holder.setData(currentList[position], longClick)
    }

    class UploadCatViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun setData(cat: Cat?, longClick: (idImage: String) -> Unit) {
            Glide.with(uploadedCat.context)
                .load(cat?.url)
                .centerCrop()
                .into(uploadedCat)

            uploadedCat.setOnLongClickListener { view ->
                cat?.let {
                    longClick.invoke(cat.id)
                }
                true
            }
        }
    }
}
