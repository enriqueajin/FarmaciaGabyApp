package com.farmaciagaby.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmaciagaby.databinding.SelectProductItemBinding
import com.farmaciagaby.models.Product

class SelectQuotationProductsAdapter(private val productList: ArrayList<Product>) :
    RecyclerView.Adapter<SelectQuotationProductsAdapter.ViewHolder>() {

    private var checkedProductsList = arrayListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.setData(product)
        holder.binding.cbSelectProduct.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkedProductsList.add(product)
            } else {
                val myProduct = checkedProductsList.single { checkedProduct -> product == checkedProduct }
                checkedProductsList.remove(myProduct)
            }
        }

        // Mark as checked a product that was added
        if (product.isChecked) {
            holder.binding.cbSelectProduct.isChecked = true
            product.isChecked = false
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun getCheckedProducts(): ArrayList<Product> {
        return checkedProductsList
    }

    fun addNewProduct(product: Product) {
        product.isChecked = true
        productList.add(product)
        notifyItemInserted(productList.size)
    }

    class ViewHolder(itemView: SelectProductItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val binding = itemView

        fun setData(product: Product) {
            binding.tvProductName.text = product.name
        }
    }
}