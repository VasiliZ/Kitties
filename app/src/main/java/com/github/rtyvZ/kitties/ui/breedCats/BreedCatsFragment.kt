package com.github.rtyvZ.kitties.ui.breedCats

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.breed_cats_fragment.*
import javax.inject.Inject

class BreedCatsFragment @Inject constructor() : DaggerFragment(R.layout.breed_cats_fragment) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BreedCatsViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val breedsCatsAdapter = BreedCatsAdapter()
    private var visibleItemCount: Int = 0
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var isLoading = true
    private var page = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        breedProgress.show()
        linearLayoutManager = LinearLayoutManager(context)
        setUpRecyclerView()
        viewModel = ViewModelProvider(this, viewModelFactory).get(BreedCatsViewModel::class.java)
        viewModel.getBreedCats(page)

        viewModel.getBreedsCatsLiveData.observe(viewLifecycleOwner, {
            breedProgress.hide()
            breedsCatsAdapter.submitList(it)
        })

        viewModel.getErrorBreedsCatsLiveData.observe(viewLifecycleOwner, {
            findNavController().navigate(R.id.noInternetFragment)
        })
    }

    private fun setUpRecyclerView() {

        breedCatsContainer.apply {
            layoutManager = linearLayoutManager
            adapter = breedsCatsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        visibleItemCount = linearLayoutManager.childCount
                        totalItemCount = linearLayoutManager.itemCount
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                        if (isLoading) {
                            if ((visibleItemCount + lastVisibleItem) >= totalItemCount) {
                                isLoading = false
                                viewModel.getBreedCats(++page)
                            }
                        }
                    }
                }
            })
        }
    }
}