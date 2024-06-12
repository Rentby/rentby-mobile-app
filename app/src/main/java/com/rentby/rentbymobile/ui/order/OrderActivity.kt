package com.rentby.rentbymobile.ui.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rentby.rentbymobile.databinding.ActivityOrderBinding
import com.rentby.rentbymobile.helper.calculateDay
import com.rentby.rentbymobile.helper.dateToDay
import com.rentby.rentbymobile.helper.dateToMilliseconds
import com.rentby.rentbymobile.helper.formatStringtoMoney

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private val viewModel: OrderViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonConfirmOrder.setOnClickListener {
            val orderId = "ORD1" // Replace with the actual order ID you want to pass
            val intent = Intent(this, OrderDetailActivity::class.java).apply {
                putExtra(OrderDetailActivity.ORDER_ID, orderId)
            }
            startActivity(intent)
            finish()
        }

        setupView()
    }

    private fun setupView() {
        val productId = intent.getStringExtra(PRODUCT_ID)
        val rentStart = intent.getLongExtra(RENT_START, 0L)
        val rentEnd = intent.getLongExtra(RENT_END, 0L)

        if (productId != null) {
            Log.d("OrderActivity", productId)
        }
        if (productId != null) {
            viewModel.getProduct(productId)
            Log.d("OrderActivity", rentStart.toString() + rentEnd.toString())
            viewModel.generateBookingData(productId, rentStart, rentEnd)
            Log.d("OrderActivity", "Product found and booking data generated")
        } else {
            Log.d("OrderActivity", "Product ID is null")
        }

        viewModel.product.observe(this) { product ->
            binding.tvProductName.text = product?.name ?: ""
            product?.image?.let { imageResId ->
                binding.imageProduct.setImageResource(imageResId)
            }
        }

        viewModel.booking.observe(this){ booking ->
            val dateRange = "${dateToDay(booking.rentStart)} - ${dateToDay(booking.rentEnd)}"
            Log.d("OrderActivity", "start $booking.rentStart")
            Log.d("OrderActivity", "end ${booking.rentEnd}")
            val duration = calculateDay(dateToMilliseconds(booking.rentStart), dateToMilliseconds(booking.rentEnd))
            val orderPrice = "$duration x ${formatStringtoMoney(booking.rentPrice)}"

            binding.tvRentDaterange.text = dateRange
            binding.tvOrderPrice.text = orderPrice

            binding.tvPriceTotal.text = formatStringtoMoney(booking.rentTotal)
            binding.tvServiceFee.text = formatStringtoMoney(booking.serviceFee)
            binding.tvDeposit.text = formatStringtoMoney(booking.deposit)
            binding.tvOrderTotal.text = formatStringtoMoney(booking.orderTotal)
        }
    }

    companion object {
        const val PRODUCT_ID = "product_id"
        const val RENT_START = "rent_start"
        const val RENT_END = "rent_end"
    }
}