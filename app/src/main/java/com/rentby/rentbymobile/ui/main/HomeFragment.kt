package com.rentby.rentbymobile.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.databinding.FragmentHomeBinding
import com.rentby.rentbymobile.ui.adapter.ProductAdapter
import com.rentby.rentbymobile.utils.GridSpacingItemDecoration

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
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
            .placeholder(R.drawable.ic_profile) // Optional: placeholder image
            .error(R.drawable.ic_profile) // Optional: error image
            .into(binding.userPhoto) // Ensure the ID is correct


        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val productAdapter = ProductAdapter(requireContext(), ProductList.getProducts())
        binding.recyclerView.adapter = productAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 16, true))
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