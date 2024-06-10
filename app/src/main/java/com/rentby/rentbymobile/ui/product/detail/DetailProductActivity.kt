package com.rentby.rentbymobile.ui.product.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityDetailProductBinding
import com.rentby.rentbymobile.databinding.ActivityMainBinding
import com.rentby.rentbymobile.helper.formatStringtoRP
import com.rentby.rentbymobile.ui.order.OrderDetailActivity

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private val viewModel: DetailProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.floatingActionButtonBack.setOnClickListener {
            finish()
        }

        val productId = intent.getStringExtra(PRODUCT_ID) ?: ""
        viewModel.getProduct(productId)

        setupView()

        setupBookingAction(productId)
    }

    private fun setupView(){
        viewModel.product.observe(this) { product ->
            product?.let {
                binding.textViewProductName.text = it.name
                binding.tvPrice.text = formatStringtoRP(it.price)
                binding.tvRatingCount.text = it.rating.toString()
                binding.tvBooked.text = it.booked.toString()
                binding.textViewLocation.text = it.location
                binding.textViewDescription.text = it.description
                it.image?.let { imageResId ->
                    binding.imageProduct.setImageResource(imageResId)
                }
            }
        }
//        viewModel.isLoading.observe(this, Observer { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        })
    }

    private fun setupBookingAction(productId: String) {
        binding.apply {
            buttonBook.setOnClickListener {
                val modal = BookingCalendarFragment.newInstance(productId) // Pass productId
                supportFragmentManager.let { modal.show(it, BookingCalendarFragment.TAG) }
            }
        }
    }

    companion object {
        const val PRODUCT_ID = ""
    }
}