package com.github.rtyvZ.kitties.ui.favoriteCats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.helpers.DragItemHelper
import kotlinx.android.synthetic.main.favorite_cats_fragment.*

class FavoriteCatsFragment : Fragment(R.layout.favorite_cats_fragment) {

    private val viewModel: FavoriteCatsViewModel by viewModels()
    private val itemTouchHelper = ItemTouchHelper(DragItemHelper { position, direction ->
        viewModel.deleteFavoriteCat(position)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterForFavCats = FavoriteCatsAdapter()

        viewModel.getFavoriteCats()
        itemTouchHelper.attachToRecyclerView(listFavoriteCat)

        listFavoriteCat.apply {
            activity?.let { activity ->
                layoutManager = LinearLayoutManager(activity)
                adapter = adapterForFavCats
            }
        }

        viewModel.getMyFavoriteCats.observe(viewLifecycleOwner, {
            adapterForFavCats.submitList(it)
        })
    }
}
