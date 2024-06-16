package com.rentby.rentbymobile.ui.adapter

import android.content.Intent
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.databinding.ItemProductBinding
import com.rentby.rentbymobile.helper.formatStringtoRP
import com.rentby.rentbymobile.ui.product.detail.DetailProductActivity

class ProductAdapter(
    private val context: Context,
    private val listProduct: List<ProductItem>?
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductItem) {
            val price = formatStringtoRP(product.price)
            val rating = String.format("%.1f", product.rating)

            binding.tvProductTitle.text = product.name
            binding.tvProductPrice.text = price
            binding.tvRatingCount.text = rating

            Glide.with(context)
                .load(product.imageUrl)
                .placeholder(R.drawable.default_image) // Placeholder image
                .error(R.drawable.default_image) // Error image if loading fails
                .into(binding.itemImage)

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailProductActivity::class.java)
                intent.putExtra(DetailProductActivity.PRODUCT_ID, product.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        listProduct?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return listProduct?.size ?: 0
    }
}