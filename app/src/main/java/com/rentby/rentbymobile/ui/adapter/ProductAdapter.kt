package com.rentby.rentbymobile.ui.adapter

import android.content.Intent
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.model.Product
import com.rentby.rentbymobile.databinding.ItemProductBinding
import com.rentby.rentbymobile.helper.formatStringtoRP
import com.rentby.rentbymobile.ui.product.detail.DetailProductActivity

class ProductAdapter(
    private val context: Context,
    private val listProduct: ArrayList<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            val price = formatStringtoRP(product.price)
            val rating = String.format("%.1f", product.rating)
            val booked = "• Disewa " + product.booked.toString()

            binding.tvProductTitle.text = product.name
            binding.tvProductPrice.text = price
            binding.tvRatingCount.text = rating
            binding.tvBooked.text = booked
            product.image?.let {
                binding.itemImage.setImageResource(it)
            } ?: binding.itemImage.setImageResource(R.drawable.default_image)

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
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
}