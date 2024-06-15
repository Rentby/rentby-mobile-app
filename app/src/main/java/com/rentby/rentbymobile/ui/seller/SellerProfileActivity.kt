package com.rentby.rentbymobile.ui.seller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.data.mock.ProductItemList
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.databinding.ActivitySellerProfileBinding
import com.rentby.rentbymobile.ui.adapter.ProductAdapter
import com.rentby.rentbymobile.utils.GridSpacingItemDecoration

class SellerProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerProfileBinding
    private val viewModel: SellerProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        intent.getStringExtra(SELLER_ID)?.let {
            viewModel.getSeller(it)
        }

        setupView()
        setupRecyclerView()
    }

    private fun setupView() {
        binding.floatingActionButtonBack.setOnClickListener {
            finish()
        }

        viewModel.seller.observe(this) { seller ->
            seller?.let {
                binding.tvSellerNameProfile.text = seller.name
                binding.tvSellerLocation.text = seller.location
                binding.tvSellerProductTotal.text = seller.productTotal.toString() + " Produk"

                Glide.with(this)
                    .load(seller.image)
                    .into(binding.imageView)

                binding.buttonMessageSeller.setOnClickListener {
                    val url = seller.whatsappLink // Replace with your desired URL
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }

                binding.floatingActionButtonShare.setOnClickListener {
                    val deepLink = generateDeepLink(seller.id)
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, deepLink)
                        type = "text/plain"
                    }
                    startActivity(Intent.createChooser(shareIntent, "Share via"))
                }

                binding.buttonSellerDescription.setOnClickListener {
                    showDescription(seller.description)
                }
            }
        }
    }

    private fun generateDeepLink(sellerId: String): String {
        return "https://open.rentby.com/seller/$sellerId"
    }

    private fun showDescription(description: String) {
        val modal = SellerDescriptionFragment.newInstance(description)
        supportFragmentManager.let { modal.show(it, SellerDescriptionFragment.TAG) }
    }

    private fun setupRecyclerView() {
        val productAdapter = ProductAdapter(this, ProductItemList.getProducts())
        binding.rvSellerProduct.adapter = productAdapter
        binding.rvSellerProduct.layoutManager = GridLayoutManager(this, 2)
        binding.rvSellerProduct.addItemDecoration(GridSpacingItemDecoration(2, 16, true))
    }

    companion object {
        val SELLER_ID = "seller_id"
    }

}