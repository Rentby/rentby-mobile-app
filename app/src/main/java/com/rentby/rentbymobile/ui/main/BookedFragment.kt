package com.rentby.rentbymobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentby.rentbymobile.data.mock.OrderList
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.databinding.FragmentBookedListBinding
import com.rentby.rentbymobile.ui.adapter.BookedListAdapter

class BookedFragment : Fragment() {

    private lateinit var binding: FragmentBookedListBinding
    private lateinit var orders: List<Order>
    private lateinit var adapter: BookedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orders = OrderList.getMockOrders()
        adapter = BookedListAdapter(requireContext(), orders)

        binding.rvBookedItem.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookedItem.adapter = adapter

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
            orders.filter { it.status == status }
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