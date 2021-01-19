package com.github.rtyvZ.kitties.ui.catsBreeds

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.cats_breeds_fragment.*
import javax.inject.Inject

class CatsBreedsFragment @Inject constructor() : DaggerFragment(R.layout.cats_breeds_fragment) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CatsBreedsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breedsAdapter = BreedsCatsAdapter {
            val bundle = Bundle()
            bundle.putParcelable(Strings.IntentConsts.DESCRIPTION_BREEDS, it)
            findNavController().navigate(R.id.breedDetails, bundle)
        }

        initRecyclerView(breedsAdapter)
        progressBreed.show()
        viewModel = ViewModelProvider(this, viewModelFactory).get(CatsBreedsViewModel::class.java)
        viewModel.getBreeds()
        viewModel.listBreeds.observe(viewLifecycleOwner, {
            progressBreed.hide()
            breedsAdapter.submitList(it)
        })
    }

    private fun initRecyclerView(breedsAdapter: BreedsCatsAdapter) {
        breedsList.apply {
            activity?.let { activity ->
                adapter = breedsAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }
    }
}