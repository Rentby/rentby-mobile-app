package com.rentby.rentbymobile.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentby.rentbymobile.data.mock.OrderList
import com.rentby.rentbymobile.databinding.ActivityBookedListBinding
import com.rentby.rentbymobile.ui.adapter.BookedListAdapter
import com.rentby.rentbymobile.ui.order.OrderDetailActivity

class BookedListActivity : AppCompatActivity(), BookedListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityBookedListBinding
    private lateinit var bookedListAdapter: BookedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookedListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get list of booked orders (mock data)
        val orders = OrderList.getMockOrders()

        // Initialize and set up the adapter
        bookedListAdapter = BookedListAdapter(this, orders)
        bookedListAdapter.setOnItemClickListener(this)

        // Set up RecyclerView
        binding.rvBookedItem.apply {
            layoutManager = LinearLayoutManager(this@BookedListActivity)
            adapter = bookedListAdapter
        }
    }

    override fun onItemClick(orderId: String) {
        // Handle item click event here, e.g., navigate to OrderDetailActivity
        val intent = Intent(this, OrderDetailActivity::class.java)
        intent.putExtra(OrderDetailActivity.ORDER_ID, orderId)
        startActivity(intent)
    }
}
