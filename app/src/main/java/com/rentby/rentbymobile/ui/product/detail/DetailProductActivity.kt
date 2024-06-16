package com.rentby.rentbymobile.ui.product.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityDetailProductBinding
import com.rentby.rentbymobile.helper.formatInttoRp
import com.rentby.rentbymobile.helper.formatStringtoRP
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.main.MainActivity
import com.rentby.rentbymobile.ui.seller.SellerProfileActivity

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private val viewModel by viewModels<DetailProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var orderActivityLauncher: ActivityResultLauncher<Intent>
    private var isDeepLink = false

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

        // Register the launcher for startActivityForResult
        orderActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Finish ProductDetailActivity if the result is OK
                finish()
            }
        }

        binding.floatingActionButtonBack.setOnClickListener {
            finish()
        }

        val productId = intent.getStringExtra(PRODUCT_ID) ?: handleDeepLink(intent)
        viewModel.getProduct(productId)

        // Check if the activity was opened via a deep link
        isDeepLink = intent.data != null

        setupView()
        setupBookingAction(productId)
        setupShareButton(productId)

        handleBackPress()

        // Set the back press callback if opened via deep link

    }

    private fun setupSellerLayout(sellerId: String) {
        binding.sellerLayout.setOnClickListener {
            val intent = Intent(this@DetailProductActivity, SellerProfileActivity::class.java)
            intent.putExtra(SellerProfileActivity.SELLER_ID, sellerId)
            startActivity(intent)
        }
    }

    private fun setupShareButton(productId: String) {
        binding.floatingActionButtonShare.setOnClickListener {
            val deepLink = generateDeepLink(productId)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, deepLink)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

    private fun generateDeepLink(productId: String): String {
        return "https://open.rentby.com/product/$productId"
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isDeepLink) {
                    val intent = Intent(this@DetailProductActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    finish()
                }
            }
        })
    }

    private fun setupView(){
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DetailProductActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        })

        // Observe the toastMessage LiveData
        viewModel.toastMessage.observe(this) { message ->
            message?.let { showToast(it) }
        }

        viewModel.product.observe(this) { product ->
            product?.let {
                binding.textViewProductName.text = it.name
                binding.tvPrice.text = formatInttoRp(it.rentPrice)
                binding.tvRatingCount.text = it.rating.toString()
                binding.tvBooked.text = it.booked.toString()
                binding.textViewLocation.text = "Testing"
                binding.textViewDescription.text = it.description
                Glide.with(this)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.default_image) // Placeholder image
                    .error(R.drawable.default_image) // Error image if loading fails
                    .into(binding.imageProduct)
                setupSellerLayout(it.sellerId)
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

    private fun handleDeepLink(intent: Intent): String {
        intent.data?.let { uri ->
            if (uri.pathSegments.size > 1) {
                return uri.pathSegments[1] // Assuming the second segment is the product ID
            }
        }
        return ""
    }

    companion object {
        const val PRODUCT_ID = ""
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}