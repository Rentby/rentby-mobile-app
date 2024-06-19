package com.rentby.rentbymobile.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.protobuf.Empty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.mock.OrderList
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.data.model.OrderItem
import com.rentby.rentbymobile.databinding.FragmentBookedListBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.adapter.BookedListAdapter
import com.rentby.rentbymobile.ui.order.OrderDetailActivity

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
        viewModel.getUserOrder(getString(R.string.user_id))
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
        }


        binding.buttonAll.setOnClickListener { filterOrders(null) }
        binding.buttonPending.setOnClickListener { filterOrders(1) }
        binding.buttonBooked.setOnClickListener { filterOrders(2) }
        binding.buttonPickedup.setOnClickListener { filterOrders(3) }
        binding.buttonReturned.setOnClickListener { filterOrders(4) }
        binding.buttonCompleted.setOnClickListener { filterOrders(5) }

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