package com.rentby.rentbymobile.ui.product.detail

import android.content.Intent
import android.net.Uri
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
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityDetailProductBinding
import com.rentby.rentbymobile.helper.formatInttoRp
import com.rentby.rentbymobile.helper.formatStringtoRP
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.main.MainActivity
import com.rentby.rentbymobile.ui.seller.SellerProfileActivity
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private val viewModel by viewModels<DetailProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
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

        val productId = intent.getStringExtra(PRODUCT_ID) ?: handleDeepLink(intent)
        viewModel.getProduct(productId)

        // Check if the activity was opened via a deep link
        isDeepLink = intent.data != null

        setupAction()
        setupBookingAction(productId)
        setupShareButton(productId)

        handleBackPress()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.product.observe(this) { product ->
            product?.let {
                binding.textViewProductName.text = it.name
                binding.tvPrice.text = formatInttoRp(it.rentPrice)
                binding.tvRatingCount.text = it.rating.toString()
                binding.tvBooked.text = it.booked.toString()
                binding.textViewDescription.text = it.description
                Glide.with(this)
                    .load(it.imageUrl)
                    .error(R.drawable.default_image)
                    .into(binding.imageProduct)
            }
        }

        viewModel.seller.observe(this) { seller ->
            seller?.let {
                binding.textViewProfileName.text = seller.name
                binding.textViewProductCount.text = seller.productTotal.toString() + " Produk"
                binding.textViewLocation.text = seller.location
                Glide.with(this)
                    .load(seller.image)
                    .circleCrop()
                    .error(R.drawable.ic_user_profile)
                    .into(binding.imageViewProfile)

                binding.btnChat.setOnClickListener {
                    if (seller.whatsappLink.isNotBlank()) {
                        val url = "https://" + seller.whatsappLink
                        val chatIntent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
                        startActivity(chatIntent)
                    } else {
                        showToast("No WhatsApp link available.")
                    }
                }

                binding.sellerLayout.setOnClickListener {
                    val intent = Intent(this@DetailProductActivity, SellerProfileActivity::class.java)
                    intent.putExtra(SellerProfileActivity.SELLER_ID, seller.id)
                    startActivity(intent)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.imageProduct.loadSkeleton()
                binding.layoutProductInfo.loadSkeleton()
                binding.textViewProductName.loadSkeleton(48)
                binding.textViewLocation.loadSkeleton(16) { color(R.color.gray_200) }
                binding.textViewProfileName.loadSkeleton(16)
                binding.textViewProductCount.loadSkeleton(8) { color(R.color.gray_200) }
                binding.textViewDescription.loadSkeleton(128) { color(R.color.gray_200) }
                binding.priceLayout.loadSkeleton()
            } else {
                binding.imageProduct.hideSkeleton()
                binding.layoutProductInfo.hideSkeleton()
                binding.textViewProductName.hideSkeleton()
                binding.textViewLocation.hideSkeleton()
                binding.textViewProfileName.hideSkeleton()
                binding.textViewProductCount.hideSkeleton()
                binding.textViewDescription.hideSkeleton()
                binding.priceLayout.hideSkeleton()
            }
        }

        viewModel.isSellerLoading.observe(this) { isSellerLoading ->
            if (isSellerLoading) {
                binding.textViewLocation.loadSkeleton(16) { color(R.color.gray_200) }
                binding.textViewProfileName.loadSkeleton(16)
                binding.textViewProductCount.loadSkeleton(8) { color(R.color.gray_200) }
            } else {
                binding.textViewLocation.hideSkeleton()
                binding.textViewProfileName.hideSkeleton()
                binding.textViewProductCount.hideSkeleton()
            }
        }

        viewModel.toastMessage.observe(this) { message ->
            message?.let { showToast(it) }
        }
    }

    private fun setupAction() {
        binding.floatingActionButtonBack.setOnClickListener {
            finish()
        }
    }

    private fun setupBookingAction(productId: String) {
        binding.apply {
            buttonBook.setOnClickListener {
                val modal = BookingCalendarFragment.newInstance(productId)
                supportFragmentManager.let { modal.show(it, BookingCalendarFragment.TAG) }
            }
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

    private fun handleDeepLink(intent: Intent): String {
        intent.data?.let { uri ->
            if (uri.pathSegments.size > 1) {
                return uri.pathSegments[1]
            }
        }
        return ""
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val PRODUCT_ID = "product_id"
    }
}
