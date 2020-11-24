package com.github.rtyvZ.kitties.ui.myCats

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.itemDecorators.RecyclerViewMargin
import kotlinx.android.synthetic.main.my_cat_fragment.*

class MyCatFragment : Fragment(R.layout.my_cat_fragment) {

    private val viewModel: MyCatsViewModel by viewModels()
    private val longClick: (idImage: String) -> (Unit) = {
        viewModel.deleteUploadedCat(it)
    }
    private val uploadedAdapter = UploadedCatAdapter(longClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModel.getMyCatsFromRemoteApi()

        viewModel.getMyCats.observe(viewLifecycleOwner, {
            uploadedAdapter.submitList(it)
        })

        viewModel.getStatusDeleteUploadedCat.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.image_deleted, Toast.LENGTH_LONG).show()
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
}
