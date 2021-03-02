package com.github.rtyvZ.kitties.ui.myUploadedCats

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.itemDecorators.RecyclerViewMargin
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.databinding.MyCatFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyCatFragment : Fragment() {

    private var _binding: MyCatFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyCatsViewModel by viewModels()
    private val longClick: (cat: Cat) -> (Unit) = { cat ->
        createDialog(cat)
    }
    private val uploadedAdapter = UploadedCatAdapter(longClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyCatFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        viewModel.errorWhileDeletingCat.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.no_connection, Toast.LENGTH_LONG).show()
        })

        viewModel.getErrorMyCats.observe(viewLifecycleOwner, {
            findNavController().navigate(R.id.action_my_cats_to_noInternetFragment)
        })

        lifecycleScope.launch {
            viewModel.myKitties.collectLatest {
                uploadedAdapter.submitData(it)
            }
        }

        viewModel.getSuccessDeleteCat.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.cat_was_deleted, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setUpRecyclerView() {
        val decoration = RecyclerViewMargin(8, 2)
        activity?.let { activity ->
            binding.uploadedCatsRecyclerView.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
