package com.github.rtyvZ.kitties.ui.favoriteCats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.helpers.DragItemHelper
import com.github.rtyvZ.kitties.databinding.FavoriteCatsFragmentBinding
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteCatsFragment : Fragment() {
    private var _binding: FavoriteCatsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteCatsViewModel by viewModels()
    private val adapterForFavCats = FavoriteCatsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteCatsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterForFavCats = FavoriteCatsAdapter()
        binding.progressFavCat.show()

        binding.listFavoriteCat.apply {
            activity?.let { activity ->
                layoutManager = LinearLayoutManager(activity)
                adapter = adapterForFavCats
                ItemTouchHelper(DragItemHelper { position, _ ->
                    this.adapter?.let {
                        viewModel.deleteFavoriteCat((it as FavoriteCatsAdapter).getCat(position))
                    }
                }).attachToRecyclerView(this)
            }
        }

        viewModel.fetchKitties.observe(viewLifecycleOwner, { data ->
            binding.progressFavCat.hide()
            lifecycleScope.launch {
                adapterForFavCats.submitData(data)
            }
        })

        viewModel.getErrorReceiveCats.observe(viewLifecycleOwner, {
            binding.progressFavCat.hide()
            findNavController().navigate(R.id.action_favorite_cats_to_noInternetFragment)
        })

        viewModel.getErrorDeleteFavoriteCats.observe(viewLifecycleOwner, {
            activity?.let {
                binding.listFavoriteCat.adapter?.notifyDataSetChanged()
                Toast.makeText(it, R.string.no_connection, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}