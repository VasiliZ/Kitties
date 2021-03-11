package com.github.rtyvZ.kitties.ui.randomCats

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.DataLoadsStateAdapter
import com.github.rtyvZ.kitties.common.Strings.IntentConsts.DOWNLOAD_IMAGE_KEY
import com.github.rtyvZ.kitties.common.helpers.DragItemHelper
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.databinding.RandomCatsFragmentBinding
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import com.github.rtyvZ.kitties.ui.services.ImageDownloadService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomCatsFragment() : Fragment() {

    private var _binding: RandomCatsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RandomCatsViewModel by viewModels()
    private lateinit var manager: LinearLayoutManager
    private lateinit var catForDownloadImage: Cat

    private val openContextMenu: (cat: Cat, view: View) -> Unit = { cat, view ->
        activity?.let { activity ->
            catForDownloadImage = cat
            activity.openContextMenu(view)
        }
    }

    private lateinit var catAdapterPaging: RandomPagingCatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RandomCatsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.show()
        viewModel.clear()

        this.registerForContextMenu(binding.listRandomCats)

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

    private fun initRecyclerView() {
        binding.listRandomCats.apply {
            activity?.let {
                catAdapterPaging = RandomPagingCatAdapter(openContextMenu, viewModel)
                adapter = catAdapterPaging
                    .withLoadStateFooter(
                        footer = DataLoadStateRandomKittiesAdapter(catAdapterPaging)
                    )
                //disable blinks in recycler view
                itemAnimator = null
                manager = LinearLayoutManager(it)
                layoutManager = manager
                ItemTouchHelper(DragItemHelper { posiiton, _ ->
                    this.adapter?.let { adapter ->
                        viewModel
                            .addToFavorites(
                                (adapter as RandomPagingCatAdapter)
                                    .getCat(posiiton)
                            )
                    }
                }).attachToRecyclerView(this)
            }
        }
    }

    private fun initObservers() {

        viewModel.getRandomCatsError.observe(viewLifecycleOwner, {
            binding.progress.hide()
            findNavController().navigate(R.id.action_list_kitties_to_noInternetFragment)
        })

        viewModel.getErrorActionWithCat.observe(viewLifecycleOwner,
            {
                activity?.let {
                    catAdapterPaging.notifyDataSetChanged()
                    Toast.makeText(it, R.string.no_connection, Toast.LENGTH_SHORT).show()
                }
            })

        lifecycleScope.launch {
            viewModel.listKitties.observe(viewLifecycleOwner, {
                binding.progress.hide()
                lifecycleScope.launch {
                    catAdapterPaging.submitData(it)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}