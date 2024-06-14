package com.rentby.rentbymobile.ui.seller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.databinding.ActivitySellerProfileBinding
import com.rentby.rentbymobile.ui.adapter.ProductAdapter
import com.rentby.rentbymobile.utils.GridSpacingItemDecoration

class SellerProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerProfileBinding
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

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val productAdapter = ProductAdapter(this, ProductList.getProducts())
        binding.rvSellerProduct.adapter = productAdapter
        binding.rvSellerProduct.layoutManager = GridLayoutManager(this, 2)
        binding.rvSellerProduct.addItemDecoration(GridSpacingItemDecoration(2, 16, true))
    }

}