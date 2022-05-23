package com.farmaciagaby.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmaciagaby.databinding.SelectProductItemBinding
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.models.Product

class CheckProductsAdapter(private var productList: MutableList<Product>) :
    RecyclerView.Adapter<CheckProductsAdapter.ViewHolder>() {

    private var checkedProductsList = arrayListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: on bind")
        val product = productList[position]
        holder.setData(product)
        holder.binding.cbSelectProduct.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (!checkedProductsList.contains(product)) {
                    checkedProductsList.add(product)
                }

            } else {
                if (checkedProductsList.contains(product)) {
                    checkedProductsList.remove(product)
                }
            }
        }

        // Mark as checked a product that was added
        updateRecycler(holder, product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun getCheckedProducts(): ArrayList<Product> {
        return checkedProductsList
    }

    fun addNewProduct(product: Product) {
        checkedProductsList.add(product)
        productList.add(product)
        notifyItemInserted(productList.size)
    }

    fun filterList(filteredList: MutableList<Product>) {
        productList = filteredList
        notifyDataSetChanged()
    }

    fun updateRecycler(holder: ViewHolder, product: Product) {
        holder.binding.cbSelectProduct.isChecked = checkedProductsList.contains(product)
    }

    class ViewHolder(itemView: SelectProductItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val binding = itemView

        fun setData(product: Product) {
            binding.tvProductName.text = product.nombre
        }
    }
}