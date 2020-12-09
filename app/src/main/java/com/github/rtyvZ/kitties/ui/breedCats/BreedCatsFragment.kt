package com.github.rtyvZ.kitties.ui.breedCats

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.github.rtyvZ.kitties.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BreedCatsFragment @Inject constructor() : DaggerFragment(R.layout.breed_cats_fragment) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BreedCatsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setUp recycler view
        viewModel = ViewModelProvider(this, viewModelFactory).get(BreedCatsViewModel::class.java)
        viewModel.getBreedCats()

        viewModel.getBreedsCatsLiveData.observe(viewLifecycleOwner, {
            //populate recycler view
        })

        viewModel.getErrorBreedsCatsLiveData.observe(viewLifecycleOwner, {
            //navigate to fragment error no connection
        })
    }
}