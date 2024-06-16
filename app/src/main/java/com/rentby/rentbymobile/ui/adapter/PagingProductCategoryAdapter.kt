package com.rentby.rentbymobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.ProductItem
import com.rentby.rentbymobile.databinding.ItemProductBinding
import com.rentby.rentbymobile.helper.formatStringtoRP
import com.rentby.rentbymobile.ui.product.detail.DetailProductActivity

class PagingProductCategoryAdapter : PagingDataAdapter<ProductItem, PagingProductCategoryAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) {
            holder.bind(product)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ProductViewHolder(private val binding: ItemProductBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductItem) {
            val price = formatStringtoRP(product.price)
            val rating = String.format("%.1f", product.rating)

            binding.tvProductTitle.text = product.name
            binding.tvProductPrice.text = price
            binding.tvRatingCount.text = rating

            Glide.with(context)
                .load(product.imageUrl)
                .placeholder(R.color.gray_200) // Placeholder image
                .error(R.drawable.default_image) // Error image if loading fails
                .into(binding.itemImage)

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailProductActivity::class.java)
                intent.putExtra(DetailProductActivity.PRODUCT_ID, product.id)
                context.startActivity(intent)
            }
        }
    }
}