package com.github.rtyvZ.kitties.ui.randomCats

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings.IntentConsts.DOWNLOAD_IMAGE_KEY
import com.github.rtyvZ.kitties.common.helpers.DragItemHelper
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import com.github.rtyvZ.kitties.ui.services.ImageDownloadService
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.random_cats_fragment.*
import javax.inject.Inject

class RandomCatsFragment : DaggerFragment(R.layout.random_cats_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RandomCatsViewModel
    private lateinit var manager: LinearLayoutManager
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var catForDownloadImage: Cat

    private val swipeCallback: (Int, Int) -> Unit = { position, direction ->
        viewModel.addToFavorites(position)
    }
    private val setLike: (Cat, StateCatVote) -> Unit = { cat, choice ->
        viewModel.voteForCat(cat, choice)
    }
    private val openContextMenu: (cat: Cat, view: View) -> Unit = { cat, view ->
        activity?.let { activity ->
            catForDownloadImage = cat
            activity.openContextMenu(view)
        }
    }
    private val catAdapter = RandomCatAdapter(setLike, openContextMenu)

    private var visibleItemCount: Int = 0
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var isLoading = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        progress.show()
        viewModel.clear()
        viewModel.getCats()
        this.registerForContextMenu(listRandomCats)

        initObservers()
        initRecyclerView()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        activity?.let {
            it.menuInflater.inflate(R.menu.download_menu, menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        activity?.let { activity ->

            when (item.itemId) {
                R.id.download_item -> {
                    val intent = Intent(activity, ImageDownloadService::class.java)
                    intent.apply {
                        this.putExtra(DOWNLOAD_IMAGE_KEY, catForDownloadImage)
                    }
                    activity.startService(intent)
                }
                else -> {
                    //nothing to call there at this moment
                }
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(RandomCatsViewModel::class.java)
    }

    private fun initRecyclerView() {
        listRandomCats.apply {
            activity?.let {
                adapter = catAdapter
                //disable blinks in recycler view
                itemAnimator?.changeDuration = 0
                manager = LinearLayoutManager(it)
                layoutManager = manager

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            visibleItemCount = manager.childCount
                            totalItemCount = manager.itemCount
                            lastVisibleItem = manager.findLastVisibleItemPosition()
                            if (isLoading) {
                                if ((visibleItemCount + lastVisibleItem) >= totalItemCount) {
                                    isLoading = false
                                    viewModel.getCats()
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    private fun initObservers() {
        viewModel.getRandomCats.observe(viewLifecycleOwner, {
            progress.hide()
            isLoading = true
            catAdapter.submitList(it)
        })

        viewModel.getRandomCatsError.observe(viewLifecycleOwner, {
            progress.hide()
            findNavController().navigate(R.id.action_list_kitties_to_noInternetFragment)
        })

        viewModel.getErrorActionWithCat.observe(viewLifecycleOwner,
            {
                activity?.let {
                    Toast.makeText(it, R.string.no_connection, Toast.LENGTH_SHORT).show()
                }
            })

        activity?.let {
            itemTouchHelper = ItemTouchHelper(DragItemHelper(swipeCallback))
            itemTouchHelper.attachToRecyclerView(listRandomCats)
        }
    }
}