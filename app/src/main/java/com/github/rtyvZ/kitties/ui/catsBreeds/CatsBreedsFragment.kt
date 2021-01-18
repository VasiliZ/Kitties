package com.github.rtyvZ.kitties.ui.catsBreeds

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.cats_breeds_fragment.*
import javax.inject.Inject

class CatsBreedsFragment @Inject constructor() : DaggerFragment(R.layout.cats_breeds_fragment) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CatsBreedsViewModel
    private val breedsAdapter = BreedsCatsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        viewModel = ViewModelProvider(this, viewModelFactory).get(CatsBreedsViewModel::class.java)
        viewModel.getBreeds()
        viewModel.listBreeds.observe(viewLifecycleOwner, {
            breedsAdapter.submitList(it)
        })
    }

    private fun initRecyclerView() {
        breedsList.apply {
            activity?.let { activity ->
                adapter = breedsAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }
    }
}