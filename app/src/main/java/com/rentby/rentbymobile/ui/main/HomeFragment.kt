package com.rentby.rentbymobile.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.FragmentHomeBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.adapter.PagingProductCategoryAdapter
import com.rentby.rentbymobile.ui.adapter.ProductAdapter
import com.rentby.rentbymobile.ui.profile.ProfileActivity
import com.rentby.rentbymobile.utils.GridSpacingItemDecoration

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onPause() {
        super.onPause()
        binding.recyclerView.layoutManager?.onSaveInstanceState()?.let { viewModel.saveRecyclerViewState(it) }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.stateInitialized()) {
            binding.recyclerView.layoutManager?.onRestoreInstanceState(
                viewModel.restoreRecyclerViewState()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val photoUrl = arguments?.getString(ARG_PHOTO_URL)
        Glide.with(this)
            .load(photoUrl)
            .circleCrop()
            .placeholder(R.drawable.ic_profile_placeholder) // Placeholder image
            .error(R.drawable.ic_profile_placeholder) // Error image if loading fails
            .into(binding.userPhoto)

        binding.userPhoto.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.searchView2.setOnClickListener {
            val mainActivity = activity as MainActivity
            val fragment = mainActivity.searchFragment.apply {
                arguments = Bundle().apply {
                    putBoolean(SearchFragment.ARG_OPEN_SEARCH_VIEW, true)
                }
            }
            mainActivity.updateSelectedNavigationItem(R.id.search)
        }

        binding.buttonHiking.setOnClickListener { viewModel.setCategory("hiking") }
        binding.buttonCosplay.setOnClickListener { viewModel.setCategory("cosplay") }

        setupRecyclerView()
        return binding.root
    }


    private fun setupRecyclerView() {
        val adapter = PagingProductCategoryAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 16, true))

        // Observe LoadState
        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            val successState = loadState.source.refresh as? LoadState.NotLoading
            successState?.let {
                binding.textViewErrorMessage.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false
            }


            // Show loading spinner during initial load or refresh.
            binding.progressBarHome.isVisible = loadState.source.refresh is LoadState.Loading
            val loadingState = loadState.source.refresh as? LoadState.Loading
            loadingState?.let {
                binding.textViewErrorMessage.isVisible = false
            }

            // Show error message if initial load or refresh fails.
            val errorState = loadState.source.refresh as? LoadState.Error
            errorState?.let {
                binding.textViewErrorMessage.isVisible = true
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            // Trigger the reload of data
            adapter.refresh()
        }

        viewModel.products.observe(viewLifecycleOwner, {
            adapter.submitData(lifecycle, it)
        })
    }

    companion object {
        private const val ARG_DISPLAY_NAME = "display_name"
        private const val ARG_PHOTO_URL = "photo_url"

        fun newInstance(displayName: String?, photoUrl: String?): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle().apply {
                putString(ARG_DISPLAY_NAME, displayName)
                putString(ARG_PHOTO_URL, photoUrl)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
