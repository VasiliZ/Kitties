package com.github.rtyvZ.kitties.ui.imageCats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.helpers.DragItemHelper
import kotlinx.android.synthetic.main.random_cats_fragment.*

class RandomCatsFragment : Fragment(R.layout.random_cats_fragment) {

    private val viewModel: RandomCatsViewModel by viewModels()

    private val swipeCallback: (Int, Int) -> Unit = { position, direction ->
        viewModel.vote(position, direction)
    }

    private val catAdapter = RandomCatAdapter()

    private lateinit var manager: LinearLayoutManager
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clear()
        viewModel.getCats()

        viewModel.getRandomCats.observe(viewLifecycleOwner, {
            catAdapter.submitList(it)
        })

        activity?.let {
            itemTouchHelper = ItemTouchHelper(DragItemHelper(swipeCallback, it))
            itemTouchHelper.attachToRecyclerView(listRandomCats)
        }

        listRandomCats.apply {
            activity?.let {
                adapter = catAdapter
                manager = LinearLayoutManager(it)
                layoutManager = manager
            }
        }
    }
}