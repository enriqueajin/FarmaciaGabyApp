package com.farmaciagaby.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmaciagaby.databinding.SelectProductItemBinding
import com.farmaciagaby.models.Product

class SelectQuotationProductsAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<SelectQuotationProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(itemView: SelectProductItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        private val binding = itemView

        fun setData(product: Product) {
            binding.tvProductName.text = product.name
        }
    }
}