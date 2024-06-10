package com.rentby.rentbymobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Order
import com.rentby.rentbymobile.databinding.ItemBookedBinding
import com.rentby.rentbymobile.ui.order.OrderDetailActivity

class BookedListAdapter(
    private val context: Context,
    private val listBooked: List<Order>
) : RecyclerView.Adapter<BookedListAdapter.BookedViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(orderId: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class BookedViewHolder(private val binding: ItemBookedBinding) : RecyclerView.ViewHolder(binding.root) {
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
            binding.root.setOnClickListener {
                listener?.onItemClick(order.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedViewHolder {
        val binding = ItemBookedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookedViewHolder, position: Int) {
        holder.bind(listBooked[position])
    }

    override fun getItemCount(): Int {
        return listBooked.size
    }
}
