package com.github.rtyvZ.kitties.ui.imageCats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.LinearManagerWithoutScroll
import kotlinx.android.synthetic.main.random_cats_fragment.*

class RandomCatsFragment : Fragment(R.layout.random_cats_fragment) {

    private val viewModel: RandomCatsViewModel by viewModels()
    private lateinit var manager: LinearManagerWithoutScroll


    private val swipeCallback: (Int, StateSwipe) -> Unit = { position, direction ->
        viewModel.vote(position, direction)
    }

    private val catAdapter = RandomCatAdapter(swipeCallback)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clear()
        viewModel.getCats()

        viewModel.getRandomCats.observe(viewLifecycleOwner, {
            catAdapter.submitList(it)
        })

        listRandomCats.apply {
            activity?.let {
                adapter = catAdapter
                manager = LinearManagerWithoutScroll(it)
                manager.setScrollEnabled(false)
                layoutManager = manager
            }
        }
    }
}