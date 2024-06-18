package com.rentby.rentbymobile.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.rentby.rentbymobile.databinding.FragmentSearchBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.adapter.PagingProductListAdapter
import com.rentby.rentbymobile.utils.GridSpacingItemDecoration

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView
            .editText
            .setOnEditorActionListener { v , actionId, event ->
                binding.searchBar.setText(binding.searchView.text)
                binding.searchView.hide()
                viewModel.setQuery(binding.searchView.text.toString())
                false
            }

        setupRecyclerView()

        // Check if the SearchView should be opened immediately
        val openSearchView = arguments?.getBoolean(ARG_OPEN_SEARCH_VIEW, false) ?: false
        if (openSearchView) {
            binding.searchView.show()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupRecyclerView() {
        val adapter = PagingProductListAdapter()
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView2.addItemDecoration(GridSpacingItemDecoration(2, 16, true))

        // Observe LoadState
        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.recyclerView2.isVisible = loadState.source.refresh is LoadState.NotLoading
            val successState = loadState.source.refresh as? LoadState.NotLoading
            successState?.let {
                binding.textViewErrorMessageSearch.isVisible = false
            }


            // Show loading spinner during initial load or refresh.
            binding.progressBarSearch.isVisible = loadState.source.refresh is LoadState.Loading
            val loadingState = loadState.source.refresh as? LoadState.Loading
            loadingState?.let {
                binding.textViewErrorMessageSearch.isVisible = false
            }

            // Show error message if initial load or refresh fails.
            val errorState = loadState.source.refresh as? LoadState.Error
            errorState?.let {
                binding.textViewErrorMessageSearch.isVisible = true
            }
        }

        viewModel.searchProducts.observe(viewLifecycleOwner, {
            adapter.submitData(lifecycle, it)
        })
    }

    companion object {
        const val ARG_OPEN_SEARCH_VIEW = "open_search_view"

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}