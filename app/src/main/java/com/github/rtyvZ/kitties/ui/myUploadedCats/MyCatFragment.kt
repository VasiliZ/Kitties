package com.github.rtyvZ.kitties.ui.myUploadedCats

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.common.itemDecorators.RecyclerViewMargin
import com.github.rtyvZ.kitties.common.models.Cat
import kotlinx.android.synthetic.main.my_cat_fragment.*

class MyCatFragment : Fragment(R.layout.my_cat_fragment) {

    private val viewModel: MyCatsViewModel by viewModels()
    private val longClick: (cat: Cat, position: Int) -> (Unit) = { cat, position ->
        viewModel.deleteUploadedCat(cat, position)
    }
    private val uploadedAdapter = UploadedCatAdapter(longClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        viewModel.errorWhileDeletingCat.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.no_connection, Toast.LENGTH_LONG).show()
        })

        viewModel.getErrorMyCats.observe(viewLifecycleOwner, {
            findNavController().navigate(R.id.action_my_cats_to_noInternetFragment)
        })

        App.DataBaseProvider.getDataBase()
            .getCatDao().let { dao ->
                dao.getAllCats()?.let {
                    it.observe(viewLifecycleOwner, { cats ->
                        cats?.let {
                            uploadedAdapter.submitList(cats)
                        }
                    })
                }
            }
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
