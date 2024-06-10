package com.rentby.rentbymobile.ui.order

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rentby.rentbymobile.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
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
            val orderId = "ORD4" // Replace with the actual order ID you want to pass
            val intent = Intent(this, OrderDetailActivity::class.java).apply {
                putExtra(OrderDetailActivity.ORDER_ID, orderId)
            }
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val SELLER_NAME = "Pendaki Makassar"
        const val PRODUCT_NAME = "Tas Ransel Gunung Carrier 50L Backsupport Water Resistant"
        const val PRODUCT_IMAGE = "https://down-id.img.susercontent.com/file/sg-11134201-7qvdi-ljd92z2nswelf2"
        const val RENT_PRICE = "40000"
        const val RENT_START = "1718064000000"
        const val RENT_END = "1718064000000"
    }
}