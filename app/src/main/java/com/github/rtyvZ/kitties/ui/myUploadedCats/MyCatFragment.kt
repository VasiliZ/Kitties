package com.github.rtyvZ.kitties.ui.myUploadedCats

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.itemDecorators.RecyclerViewMargin
import com.github.rtyvZ.kitties.common.models.Cat
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.my_cat_fragment.*
import javax.inject.Inject

class MyCatFragment @Inject constructor() : DaggerFragment(R.layout.my_cat_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MyCatsViewModel
    private val longClick: (cat: Cat) -> (Unit) = { cat ->
        createDialog(cat)
    }
    private val uploadedAdapter = UploadedCatAdapter(longClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MyCatsViewModel::class.java)
        setUpRecyclerView()

        viewModel.errorWhileDeletingCat.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.no_connection, Toast.LENGTH_LONG).show()
        })

        viewModel.getErrorMyCats.observe(viewLifecycleOwner, {
            findNavController().navigate(R.id.action_my_cats_to_noInternetFragment)
        })

        viewModel.getKittiesFromDB()?.observe(viewLifecycleOwner, { cats ->
            cats?.let {
                uploadedAdapter.submitList(cats)
            }
        })
        viewModel.getSuccessDeleteCat.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.cat_was_deleted, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setUpRecyclerView() {
        val decoration = RecyclerViewMargin(8, 2)
        activity?.let { activity ->
            uploadedCatsRecyclerView.apply {
                this.addItemDecoration(decoration)
                adapter = uploadedAdapter
                layoutManager = GridLayoutManager(activity, 2)
            }
        }
    }

    private fun createDialog(cat: Cat) {
        activity?.let { activity ->
            AlertDialog.Builder(activity)
                .setTitle(R.string.alert)
                .setPositiveButton(R.string.yes)
                { _, _ ->
                    viewModel.deleteUploadedCat(cat)
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .setMessage(R.string.confirm_delete)
                .create()
                .show()
        }
    }
}
