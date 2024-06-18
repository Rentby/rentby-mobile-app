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
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.databinding.ItemBookedBinding
import com.rentby.rentbymobile.ui.order.OrderDetailActivity
import java.text.NumberFormat
import java.util.*

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

            val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            formatter.maximumFractionDigits = 0
            val formattedTotal = formatter.format(order.rentTotal)

            val ssb = SpannableStringBuilder()

            val totalText = "Total: "
            val spannableTotal = SpannableString(totalText)
            spannableTotal.setSpan(ForegroundColorSpan(Color.WHITE), 0, totalText.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.append(spannableTotal)

            val spannablePrice = SpannableString(formattedTotal)
            spannablePrice.setSpan(ForegroundColorSpan(Color.parseColor("#FFC947")), 0, formattedTotal.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.append(spannablePrice)

            binding.rentTotal.text = ssb

            binding.orderStatus.text = getStatusText(order.status)
            binding.orderStatus.setTextColor(getStatusColor(order.status))

            val imageRes = order.image ?: R.drawable.default_image
            binding.orderImage.setImageResource(imageRes)

            binding.root.setOnClickListener {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra(OrderDetailActivity.ORDER_ID, order.id)
                context.startActivity(intent)
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

        private fun getStatusColor(status: Int): Int {
            return when (status) {
                1 -> Color.parseColor("#0A1931") // Gray for Pending
                2 -> Color.parseColor("#0000FF") // Blue for Booked
                3 -> Color.parseColor("#FFA500") // Orange for Picked Up
                4 -> Color.parseColor("#800080") // Purple for Returned
                5 -> Color.parseColor("#008000") // Green for Completed
                else -> Color.parseColor("#000000")
            }
        }
    }
}
