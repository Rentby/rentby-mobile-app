package com.rentby.rentbymobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentby.rentbymobile.data.mock.OrderList
import com.rentby.rentbymobile.databinding.FragmentBookedListBinding
import com.rentby.rentbymobile.ui.adapter.BookedListAdapter

class BookedFragment : Fragment() {

    private var _binding: FragmentBookedListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orders = OrderList.getMockOrders().filter { it.status == 2 }

        binding.rvBookedItem.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookedItem.adapter = BookedListAdapter(orders)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}