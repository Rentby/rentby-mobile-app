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
import com.rentby.rentbymobile.data.response.ResultsItem
import com.rentby.rentbymobile.databinding.ItemProductBinding
import com.rentby.rentbymobile.helper.formatInttoRp
import com.rentby.rentbymobile.helper.formatStringtoRP
import com.rentby.rentbymobile.ui.product.detail.DetailProductActivity

class PagingProductCategotyAdapter : PagingDataAdapter<ResultsItem, PagingProductCategotyAdapter.ProductViewHolder>(DIFF_CALLBACK) {

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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ProductViewHolder(private val binding: ItemProductBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ResultsItem) {
            val price = formatInttoRp(product.rentPrice ?: 0)
            val rating = String.format("%.1f", product.rating?.toFloatOrNull() ?: 0.0f)

            binding.tvProductTitle.text = product.productName
            binding.tvProductPrice.text = price
            binding.tvRatingCount.text = rating

            Glide.with(context)
                .load(product.urlPhoto)
                .placeholder(R.drawable.default_image) // Placeholder image
                .error(R.drawable.default_image) // Error image if loading fails
                .into(binding.itemImage)

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailProductActivity::class.java)
                intent.putExtra(DetailProductActivity.PRODUCT_ID, product.productId)
                context.startActivity(intent)
            }
        }
    }
}