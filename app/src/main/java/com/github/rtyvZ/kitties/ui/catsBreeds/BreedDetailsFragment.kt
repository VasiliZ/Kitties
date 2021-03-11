package com.github.rtyvZ.kitties.ui.catsBreeds

import android.os.Bundle
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.databinding.BreedInnerItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreedDetailsFragment : Fragment() {
    private var _binding: BreedInnerItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BreedInnerItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breeds = arguments?.getParcelable<CatBreed>(Strings.IntentConsts.DESCRIPTION_BREEDS)

        activity?.let {
            Glide.with(it).load(breeds?.image?.url).centerCrop().into(binding.breedCatImage)
        }
        binding.textDescription.text = createIndentedText(breeds?.description.toString(), 32, 0)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}