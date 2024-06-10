package com.rentby.rentbymobile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentby.rentbymobile.data.mock.OrderList
import com.rentby.rentbymobile.databinding.ActivityBookedListBinding
import com.rentby.rentbymobile.ui.adapter.BookedListAdapter

class BookedListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookedListBinding
    private lateinit var orderAdapter: BookedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookedListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orders = OrderList.getMockOrders()
        orderAdapter = BookedListAdapter(orders)

        binding.rvBookedItem.apply {
            layoutManager = LinearLayoutManager(this@BookedListActivity)
            adapter = orderAdapter
        }
    }
}
