package com.github.rtyvZ.kitties.ui.imageCats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import kotlinx.android.synthetic.main.random_cats_fragment.*

class RandomCatsFragment : Fragment(R.layout.random_cats_fragment) {

    private val viewModel: RandomCatsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clear()
        viewModel.getCats()

        val catAdapter = RandomCatAdapter()
        viewModel.getRandomCats.observe(viewLifecycleOwner, {
            catAdapter.submitList(it)
        })

        listRandomCats.apply {
            activity?.let {
                adapter = catAdapter
                layoutManager = LinearLayoutManager(it)
            }
        }
    }
}