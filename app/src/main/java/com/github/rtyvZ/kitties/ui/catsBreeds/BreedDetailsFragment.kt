package com.github.rtyvZ.kitties.ui.catsBreeds

import android.os.Bundle
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.view.View
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.common.models.CatBreed
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.breed_inner_item.*
import javax.inject.Inject

class BreedDetailsFragment @Inject constructor() : DaggerFragment(R.layout.breed_inner_item) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val breeds = arguments?.getParcelable<CatBreed>(Strings.IntentConsts.DESCRIPTION_BREEDS)
        activity?.let {
            Glide.with(it).load(breeds?.image?.url).centerCrop().into(breedCatImage)
        }
        textDescription.text = createIndentedText(breeds?.description.toString(), 32, 0)
    }

    private fun createIndentedText(
        text: String,
        marginFirstLine: Int,
        marginNextLine: Int
    ): SpannableString {
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            LeadingMarginSpan.Standard(marginFirstLine, marginNextLine),
            0,
            text.length,
            0
        )
        return spannableString
    }
}