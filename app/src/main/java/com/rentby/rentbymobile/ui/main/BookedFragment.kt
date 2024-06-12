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

//    private var _binding: FragmentBookedListBinding? = null
//    private val binding get() = _binding!!

    private lateinit var binding: FragmentBookedListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orders = OrderList.getMockOrders()

        binding.rvBookedItem.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookedItem.adapter = BookedListAdapter(requireContext(), orders)
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