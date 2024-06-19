package com.rentby.rentbymobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.data.model.OrderItem
import com.rentby.rentbymobile.databinding.ItemBookedBinding
import com.rentby.rentbymobile.helper.calculateDay
import com.rentby.rentbymobile.helper.calculateDayFromDate
import com.rentby.rentbymobile.helper.formatDateToReadable
import com.rentby.rentbymobile.helper.formatStringtoRP
import com.rentby.rentbymobile.ui.order.OrderDetailActivity
import java.text.NumberFormat
import java.util.*

class BookedListAdapter(
    private val context: Context,
    private var orders: List<OrderItem>
) : RecyclerView.Adapter<BookedListAdapter.BookedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedViewHolder {
        val binding = ItemBookedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookedViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: BookedViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<OrderItem>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    inner class BookedViewHolder(private val binding: ItemBookedBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderItem) {
            binding.productName.text = order.productName
            binding.orderTime.text = "${formatDateToReadable(order.rentStart)} - ${formatDateToReadable(order.rentEnd)}"
            binding.rentDuration.text = "Duration: ${calculateDayFromDate(order.rentStart, order.rentEnd)} days"
            binding.rentTotal.text = "${formatStringtoRP(order.orderTotal.toString())}"

            val statusText = getStatusText(order.status.toIntOrNull() ?: 7)
            val statusColor = getStatusColor(order.status.toIntOrNull() ?: 7)
            binding.orderStatus.text = statusText
            binding.orderStatus.setTextColor(statusColor)

            Glide.with(context)
                .load(order.imageUrl)
                .placeholder(R.color.gray_200) // Placeholder image
                .error(R.drawable.default_image) // Error image if loading fails
                .into(binding.orderImage)

            binding.root.setOnClickListener {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra(OrderDetailActivity.ORDER_ID, order.orderId)
                context.startActivity(intent)
            }
        }

        private fun getStatusText(status: Int): String {
            return when (status) {
                1 -> "Pending"
                2 -> "Booked"
                3 -> "Active"
                4 -> "Late"
                5 -> "Completed"
                6 -> "Canceled"
                else -> "Unknown"
            }
        }

        private fun getStatusColor(status: Int): Int {
            return when (status) {
                1 -> Color.parseColor("#0A1931") // Gray for Pending
                2 -> Color.parseColor("#0000FF") // Blue for Booked
                3 -> Color.parseColor("#FFA500") // Orange for Active
                4 -> Color.parseColor("#800080") // Purple for Late
                5 -> Color.parseColor("#008000") // Green for Completed
                6 -> Color.parseColor("#FF0000") // Red for Canceled
                else -> Color.parseColor("#000000") // Black for Unknown
            }
        }
    }
}
