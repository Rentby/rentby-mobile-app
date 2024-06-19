package com.rentby.rentbymobile.ui.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityOrderBinding
import com.rentby.rentbymobile.helper.calculateDay
import com.rentby.rentbymobile.helper.dateToDay
import com.rentby.rentbymobile.helper.dateToMilliseconds
import com.rentby.rentbymobile.helper.formatInttoMoney
import com.rentby.rentbymobile.helper.formatInttoRp
import com.rentby.rentbymobile.helper.formatStringtoMoney
import com.rentby.rentbymobile.ui.ViewModelFactory
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private val viewModel by viewModels<OrderViewModel> {
        ViewModelFactory.getInstance(this)
    }
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
            val userId = getString(R.string.user_id)
            val productId = intent.getStringExtra(PRODUCT_ID)
            val rentStart = intent.getLongExtra(RENT_START, 0L)
            val rentEnd = intent.getLongExtra(RENT_END, 0L)
            if (productId != null) {
                viewModel.makeOrder(productId, userId, rentStart, rentEnd)
            }
        }

        setupView()
        setupSkeleton()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.orderId.observe(this) {
            val intent = Intent(this, OrderDetailActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(OrderDetailActivity.ORDER_ID, it)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun setupSkeleton() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.tvSellerName.loadSkeleton(22)
                binding.tvProductName.loadSkeleton(56)
                binding.imageProduct.loadSkeleton()
                binding.tvRentDaterange.loadSkeleton(16)
                binding.tvOrderPrice.loadSkeleton(10)
            } else {
                binding.tvSellerName.hideSkeleton()
                binding.tvProductName.hideSkeleton()
                binding.imageProduct.hideSkeleton()
                binding.tvRentDaterange.hideSkeleton()
                binding.tvOrderPrice.hideSkeleton()
            }
        }

        viewModel.isOrderLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.titleRentSummary.loadSkeleton(17)
                binding.textView14.loadSkeleton(10)
                binding.textView16.loadSkeleton(10)
                binding.textView5.loadSkeleton(10)
                binding.textView18.loadSkeleton(14)
                binding.tvPriceTotal.loadSkeleton(8)
                binding.tvServiceFee.loadSkeleton(8)
                binding.tvDeposit.loadSkeleton(8)
                binding.tvOrderTotal.loadSkeleton(8)

                binding.buttonConfirmOrder.loadSkeleton()
            } else {
                binding.titleRentSummary.hideSkeleton()
                binding.textView14.hideSkeleton()
                binding.textView16.hideSkeleton()
                binding.textView5.hideSkeleton()
                binding.textView18.hideSkeleton()
                binding.tvPriceTotal.hideSkeleton()
                binding.tvServiceFee.hideSkeleton()
                binding.tvDeposit.hideSkeleton()
                binding.tvOrderTotal.hideSkeleton()

                binding.buttonConfirmOrder.hideSkeleton()
            }
        }
    }

    private fun setupView() {
        val productId = intent.getStringExtra(PRODUCT_ID)
        val rentStart = intent.getLongExtra(RENT_START, 0L)
        val rentEnd = intent.getLongExtra(RENT_END, 0L)

        if (productId != null) {
            viewModel.getProduct(productId)
            viewModel.estimateOrder(productId, rentStart, rentEnd)
        } else {
            Log.d("OrderActivity", "Product ID is null")
        }

        viewModel.product.observe(this) { product ->
            binding.tvProductName.text = product?.name ?: ""
            Glide.with(this)
                .load(product?.imageUrl)
                .placeholder(R.color.gray_200)
                .error(R.drawable.default_image)
                .into(binding.imageProduct)
        }

        viewModel.estimateOrderResponse.observe(this){ order ->
            val dateRange = "${dateToDay(order.rentStart)} - ${dateToDay(order.rentEnd)}"
            Log.d("OrderActivity", "start ${order.rentStart}")
            Log.d("OrderActivity", "end ${order.rentEnd}")
            val duration = calculateDay(dateToMilliseconds(order.rentStart), dateToMilliseconds(order.rentEnd))
            val orderPrice = "$duration x ${formatInttoMoney(order.rentPrice)}"

            binding.tvRentDaterange.text = dateRange
            binding.tvOrderPrice.text = orderPrice

            binding.tvPriceTotal.text = formatInttoMoney(order.rentTotal)
            binding.tvServiceFee.text = formatInttoMoney(order.serviceFee)
            binding.tvDeposit.text = formatInttoMoney(order.deposit)
            binding.tvOrderTotal.text = formatInttoMoney(order.orderTotal)
        }
    }

    companion object {
        const val PRODUCT_ID = "product_id"
        const val RENT_START = "rent_start"
        const val RENT_END = "rent_end"
    }
}