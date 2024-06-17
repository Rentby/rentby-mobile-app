package com.rentby.rentbymobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.databinding.ItemBookedBinding
import com.rentby.rentbymobile.ui.order.OrderDetailActivity
import com.rentby.rentbymobile.ui.product.detail.DetailProductActivity

class BookedListAdapter(
    private val context: Context,
    private var orders: List<Order>
) : RecyclerView.Adapter<BookedListAdapter.BookedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedViewHolder {
        val binding = ItemBookedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookedViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    inner class BookedViewHolder(private val binding: ItemBookedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.productName.text = order.productName
            binding.orderTime.text = order.orderTime
            binding.rentDuration.text = "Duration: ${order.rentDuration} days"
            binding.rentTotal.text = "Total: ${order.rentTotal}"
            binding.orderStatus.text = getStatusText(order.status)

            val imageRes = order.image ?: R.drawable.default_image // Provide a default image
            binding.orderImage.setImageResource(imageRes)

            binding.root.setOnClickListener {
                // Handle item click
            }
        }

        private fun getStatusText(status: Int): String {
            return when (status) {
                1 -> "Pending"
                2 -> "Booked"
                3 -> "Picked Up"
                4 -> "Returned"
                5 -> "Completed"
                else -> "Unknown"
            }
        }
    }
}
