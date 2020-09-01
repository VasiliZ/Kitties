package com.github.rtyvZ.kitties.ui.imageCats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.helpers.DragItemHelper
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.extantions.hide
import com.github.rtyvZ.kitties.extantions.show
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.random_cats_fragment.*

class RandomCatsFragment : Fragment(R.layout.random_cats_fragment) {

    private val viewModel: RandomCatsViewModel by viewModels()

    private val swipeCallback: (Int, Int) -> Unit = { position, direction ->
        viewModel.vote(position, direction)
    }

    private val setLike: (Cat, StateCatVote) -> Unit = { cat, choice ->
        viewModel.voteForCat(cat, choice)
    }

    private val catAdapter = RandomCatAdapter(setLike)

    private lateinit var manager: LinearLayoutManager
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress.show()
        viewModel.clear()
        viewModel.getCats()

        viewModel.getRandomCats.observe(viewLifecycleOwner, {
            progress.hide()
            catAdapter.submitList(it)
        })

        viewModel.getRandomCatsError.observe(viewLifecycleOwner, {
            progress.hide()
            Snackbar.make(randomCatConteiner, it.message.toString(), Snackbar.LENGTH_LONG).show()
        })

        viewModel.getErrorVoteCat.observe(viewLifecycleOwner,
            {
                Snackbar.make(randomCatConteiner, it.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
            })

        activity?.let {
            itemTouchHelper = ItemTouchHelper(DragItemHelper(swipeCallback, it))
            itemTouchHelper.attachToRecyclerView(listRandomCats)
        }

        listRandomCats.apply {
            activity?.let {
                adapter = catAdapter

                //disable blinks in recycler view
                itemAnimator?.changeDuration = 0
                manager = LinearLayoutManager(it)
                layoutManager = manager
            }
        }
    }
}