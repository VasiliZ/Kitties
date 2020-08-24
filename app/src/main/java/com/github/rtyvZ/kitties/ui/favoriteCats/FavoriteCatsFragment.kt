package com.github.rtyvZ.kitties.ui.favoriteCats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.rtyvZ.kitties.R
import kotlinx.android.synthetic.main.favorite_cats_fragment.*

class FavoriteCatsFragment : Fragment(R.layout.favorite_cats_fragment) {

    private val viewModel: FavoriteCatsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}