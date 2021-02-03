package com.github.rtyvZ.kitties.ui.catsBreeds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.cats_breeds_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class CatsBreedsFragment @Inject constructor() : Fragment(R.layout.cats_breeds_fragment) {

    private val viewModel: CatsBreedsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breedsAdapter = BreedsCatsAdapter {
            val bundle = Bundle()
            bundle.putParcelable(Strings.IntentConsts.DESCRIPTION_BREEDS, it)
            findNavController().navigate(R.id.breedDetails, bundle)
        }

        initRecyclerView(breedsAdapter)
        progressBreed.show()
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