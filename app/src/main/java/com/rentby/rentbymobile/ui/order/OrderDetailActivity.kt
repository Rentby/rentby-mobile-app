package com.rentby.rentbymobile.ui.order

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityOrderDetailBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.payment.PaymentActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private val viewModel by viewModels<OrderDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    @RequiresApi(Build.VERSION_CODES.O)
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

        setupView(this)
        setupAction()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val orderId = intent.getStringExtra(ORDER_ID) ?: ""
        viewModel.getOrderDetail(orderId)
    }

    private fun setupAction(){
        binding.arrowLeft.setOnClickListener {
            finish()
        }
    }

    private fun setupView(context: Context){
        viewModel.orderDetail.observe(this) { order ->
            order?.let {
                binding.buttonPayNow.setOnClickListener {
                    if (order.snapToken != "") {
                        val intent = Intent(context, PaymentActivity::class.java)
                        intent.putExtra(PaymentActivity.SNAP_TOKEN, order.snapToken)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(this, "Try Again Later", Toast.LENGTH_LONG).show()
                    }
                }

                val rentStartDate = formatDate(order.rentStart)
                val rentEndDate = formatDate(order.rentEnd)
                val rentDate = "$rentStartDate - $rentEndDate"

                val rentPricePerDay = order.rentPrice.toInt()
                val formattedRentPricePerDay = NumberFormat.getNumberInstance(Locale("id", "ID")).format(rentPricePerDay)
                val orderPrice = "${order.rentDuration} x $formattedRentPricePerDay"

                val formattedTotalRentPrice = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.rentTotal) // total harga rental
                val formattedDeposit = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.deposit)
                val formattedServiceFee = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.serviceFee.toInt())


                val totalAmount = order.rentTotal + order.serviceFee + order.deposit
                val orderTotal = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalAmount)

                val lateCharge = "${order.lateDuration} x $formattedRentPricePerDay = "
                val formattedTotalCharge = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.lateCharge)

                val status = when (order.status) {
                    1 -> {
                        binding.layoutPaymentMethod.visibility = View.GONE
                        binding.layoutPickUpTime.visibility = View.GONE
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutPickUpLocation.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonCancelOrder.visibility = View.VISIBLE
                        "Waiting for Payment"
                    }
                    2 -> {
                        binding.layoutPickUpTime.visibility = View.GONE
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        "Order Ready for Pickup"
                    }
                    3 -> {
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        "Order is in Renting Date"
                    }
                    4 -> {
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        binding.cardLateCharge.visibility = View.VISIBLE
                        "Order Needs to be Returned"
                    }
                    5 -> {
                        binding.buttonPayNow.visibility = View.GONE
                        if(order.isRated){ binding.layoutReview.visibility = View.GONE }
                        "Order Finished"
                    }
                    else -> "Unknown Status"
                }

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
                binding.tvServiceFee.text = formattedServiceFee
                binding.tvDeposit.text = formattedDeposit
                binding.tvOrderTotal.text = orderTotal

                binding.tvCharge.text = lateCharge
                binding.tvTotalCharge.text = formattedTotalCharge

                Glide.with(this)
                    .load(order.imageUrl)
                    .placeholder(R.color.gray_200)
                    .error(R.drawable.default_image)
                    .into(binding.imageProduct)
            }
        }
    }
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    companion object {
        const val ORDER_ID = "order_id"
    }
}