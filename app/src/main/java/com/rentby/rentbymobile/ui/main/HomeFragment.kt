package com.rentby.rentbymobile.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.FragmentHomeBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.adapter.PagingProductCategotyAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val photoUrl = arguments?.getString(ARG_PHOTO_URL)
        Glide.with(this)
            .load(photoUrl)
            .circleCrop()
            .placeholder(R.drawable.ic_profile) // Placeholder image
            .error(R.drawable.ic_profile) // Error image if loading fails
            .into(binding.userPhoto)

        binding.userPhoto.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }


        setupCategory()
        setupRecyclerView()

        return binding.root
    }

    private fun setupCategory() {
        binding.radio0.setOnClickListener {
            viewModel.setCategory("hiking")
        }
        binding.radio0.setOnClickListener {
            viewModel.setCategory("cosplay")
        }
    }

    private fun setupRecyclerView() {
        val adapter = PagingProductCategotyAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 16, true))

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
