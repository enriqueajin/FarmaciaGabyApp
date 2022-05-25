package com.farmaciagaby.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmaciagaby.databinding.ManageProductItemBinding
import com.farmaciagaby.models.Product

class ManageProductsAdapter(
    private var productList: MutableList<Product>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<ManageProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ManageProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.setData(product)

        holder.binding.ivEdit.setOnClickListener {
            onClickListener.onEditClick(product)
        }

        holder.binding.ivDelete.setOnClickListener {
            onClickListener.onDeleteClick(product)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(itemView: ManageProductItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val binding = itemView

        fun setData(product: Product) {
            binding.tvProductName.text = product.nombre
        }
    }

    class OnClickListener(
        val deleteListener: (product: Product) -> Unit,
        val editListener: (product: Product) -> Unit
    ) {
        fun onDeleteClick(product: Product) = deleteListener(product)
        fun onEditClick(product: Product) = editListener(product)
    }

}