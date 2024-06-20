package com.rentby.rentbymobile.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.OrderItem
import com.rentby.rentbymobile.databinding.FragmentBookedListBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.adapter.BookedListAdapter

class BookedFragment : Fragment() {

    private lateinit var binding: FragmentBookedListBinding
    private var orders: List<OrderItem> = emptyList()
    private lateinit var adapter: BookedListAdapter

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getUserOrder()
        binding = FragmentBookedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookedListAdapter(requireContext(), orders)
        binding.rvBookedItem.adapter = adapter
        binding.rvBookedItem.layoutManager = LinearLayoutManager(requireContext())

        viewModel.orders.observe(viewLifecycleOwner) { newOrders ->
            orders = newOrders
            adapter.updateOrders(orders)
            filterOrders(viewModel.orderFilter.value)
            binding.swipeRefreshLayout.isRefreshing = false  // Stop refreshing animation
        }

        viewModel.orderFilter.observe(viewLifecycleOwner) { filter ->
            filterOrders(filter)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            // Trigger data refresh
            viewModel.getUserOrder()
        }

        binding.buttonAll.setOnClickListener { viewModel.setOrderFilter(null) }
        binding.buttonPending.setOnClickListener { viewModel.setOrderFilter(1) }
        binding.buttonBooked.setOnClickListener { viewModel.setOrderFilter(2) }
        binding.buttonActive.setOnClickListener { viewModel.setOrderFilter(3) }
        binding.buttonLate.setOnClickListener { viewModel.setOrderFilter(4) }
        binding.buttonCompleted.setOnClickListener { viewModel.setOrderFilter(5) }
        binding.buttonCanceled.setOnClickListener { viewModel.setOrderFilter(6) }

        binding.buttonHelp.setOnClickListener {
            val url = "https://frontend-dot-rent-by.et.r.appspot.com/"
            val chatIntent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
            startActivity(chatIntent)
        }

    }

    private fun filterOrders(status: Int?) {
        val filteredOrders = if (status == null) {
            orders
        } else {
            orders.filter { it.status.toIntOrNull() == status }
        }
        adapter.updateOrders(filteredOrders)
    }

    companion object {
        private const val ARG_DISPLAY_NAME = "display_name"
        private const val ARG_PHOTO_URL = "photo_url"

        fun newInstance(displayName: String?, photoUrl: String?): BookedFragment {
            val fragment = BookedFragment()
            val bundle = Bundle().apply {
                putString(ARG_DISPLAY_NAME, displayName)
                putString(ARG_PHOTO_URL, photoUrl)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}