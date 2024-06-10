package com.rentby.rentbymobile.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.databinding.ItemBookedBinding

class BookedListAdapter(private val orders: List<Order>) : RecyclerView.Adapter<BookedListAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: ItemBookedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                orderImage.setImageResource(order.image ?: R.drawable.product)
                productName.text = order.productName
                orderTime.text = order.orderTime
                rentDuration.text = "${order.rentDuration} days"
                rentTotal.text = "$${order.rentTotal}"
                orderStatus.text = when (order.status) {
                    0 -> "Pending"
                    1 -> "Confirmed"
                    2 -> "Completed"
                    else -> "Unknown"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookedBinding.inflate(inflater, parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size
}
