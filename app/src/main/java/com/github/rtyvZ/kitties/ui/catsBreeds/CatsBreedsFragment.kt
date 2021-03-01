package com.github.rtyvZ.kitties.ui.catsBreeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.DataLoadsStateAdapter
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.databinding.CatsBreedsFragmentBinding
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatsBreedsFragment : Fragment(R.layout.cats_breeds_fragment) {

    private val viewModel: CatsBreedsViewModel by viewModels()
    private var _bindings: CatsBreedsFragmentBinding? = null
    private val bindings get() = _bindings!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindings = CatsBreedsFragmentBinding.inflate(inflater, container, false)
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breedsPagingAdapter = BreedsPagingCatsAdapter {
            val bundle = Bundle()
            bundle.putParcelable(Strings.IntentConsts.DESCRIPTION_BREEDS, it)
            findNavController().navigate(R.id.breedDetails, bundle)
        }

        initRecyclerView(breedsPagingAdapter)
        bindings.progressBreed.show()

        lifecycleScope.launch {
            viewModel.breeds.collectLatest {
                bindings.progressBreed.hide()
                breedsPagingAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView(breedsAdapter: BreedsPagingCatsAdapter) {
        bindings.breedsList.apply {
            activity?.let { activity ->
                adapter = breedsAdapter.withLoadStateFooter(
                    footer = DataLoadsStateAdapter(breedsAdapter)
                )
                layoutManager = LinearLayoutManager(activity)
            }
        }
    }
}