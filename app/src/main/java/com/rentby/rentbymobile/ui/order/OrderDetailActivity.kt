package com.rentby.rentbymobile.ui.order

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityOrderDetailBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private val viewModel: OrderDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val orderId = intent.getStringExtra(ORDER_ID) ?: ""
        viewModel.getOrderDetail(orderId)

        setupView()
    }

    private fun setupView(){
        viewModel.orderDetail.observe(this) { order ->
            order?.let {
                val rentStartDate = formatDate(order.rentStart)
                val rentEndDate = formatDate(order.rentEnd)
                val rentDate = "$rentStartDate - $rentEndDate"

                // Calculate the number of rental days
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDate = dateFormat.parse(order.rentStart)
                val endDate = dateFormat.parse(order.rentEnd)
                val rentalDays = if (startDate != null && endDate != null) {
                    TimeUnit.DAYS.convert(endDate.time - startDate.time, TimeUnit.MILLISECONDS).toInt()
                } else {
                    1 // Default to 1 day if date parsing fails
                }

                val rentPricePerDay = order.rentPrice.toInt()
                val totalRentPrice = rentalDays * rentPricePerDay // total harga rental
                val formattedRentPricePerDay = NumberFormat.getNumberInstance(Locale("id", "ID")).format(rentPricePerDay)
                val formattedTotalRentPrice = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalRentPrice) // total harga rental
                val orderPrice = "$rentalDays x $formattedRentPricePerDay"

                val formattedRentFee = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.serviceFee.toInt())

                val totalAmount = totalRentPrice + order.serviceFee.toInt()
                val orderTotal = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalAmount)
                val (status, color) = when (order.status) {
                    1 -> {
                        binding.layoutPickUpTime.visibility = View.GONE
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutPickUpLocation.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        "Waiting for Payment" to R.color.status_waiting_payment
                    }
                    2 -> {
                        binding.layoutPickUpTime.visibility = View.GONE
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        "Order Ready for Pickup" to R.color.status_ready_pickup
                    }
                    3 -> {
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        "Order is in Renting Date" to R.color.status_in_renting_date
                    }
                    4 -> {
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        "Order Needs to be Returned" to R.color.status_needs_return
                    }
                    5 -> {
                        binding.buttonPayNow.visibility = View.GONE
                        if(order.isRated){ binding.layoutReview.visibility = View.GONE }
                        "Order Finished" to R.color.status_finished
                    }
                    else -> "Unknown Status" to R.color.status_unknown
                }

                setStatusBarColor(ContextCompat.getColor(this, color))

                // Update UI with order details
                binding.tvOrderId.text = order.id
                binding.tvOrderStatus.text = status
                binding.tvOrderDate.text = order.orderTime
                binding.tvPickUpDatetime.text = order.pickupTime
                binding.tvReturnDatetime.text = order.returnTime
                binding.tvPayment.text = order.payment

                binding.tvSellerName.text = order.sellerName
                binding.tvProductName.text = order.productName
                binding.tvRentDaterange.text = rentDate
                binding.tvOrderPrice.text = orderPrice

                binding.tvPickUpLocation.text = order.pickupLocation

                binding.tvPriceTotal.text = formattedTotalRentPrice
                binding.tvServiceFee.text = formattedRentFee
                binding.tvOrderTotal.text = orderTotal
                order.image?.let {
                    binding.imageProduct.setImageResource(it)
                }
            }
        }
    }

    private fun setStatusBarColor(color: Int) {
        binding.statusCard.setCardBackgroundColor(color)
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    companion object {
        const val ORDER_ID = ""
    }
}