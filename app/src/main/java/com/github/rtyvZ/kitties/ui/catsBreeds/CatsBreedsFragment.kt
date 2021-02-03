package com.github.rtyvZ.kitties.ui.catsBreeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.databinding.CatsBreedsFragmentBinding
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import dagger.hilt.android.AndroidEntryPoint

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

        val breedsAdapter = BreedsCatsAdapter {
            val bundle = Bundle()
            bundle.putParcelable(Strings.IntentConsts.DESCRIPTION_BREEDS, it)
            findNavController().navigate(R.id.breedDetails, bundle)
        }

        initRecyclerView(breedsAdapter)
        bindings.progressBreed.show()
        viewModel.getBreeds()
        viewModel.listBreeds.observe(viewLifecycleOwner, {
            bindings.progressBreed.hide()
            breedsAdapter.submitList(it)
        })
    }

    private fun initRecyclerView(breedsAdapter: BreedsCatsAdapter) {
        bindings.breedsList.apply {
            activity?.let { activity ->
                adapter = breedsAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }
    }
}