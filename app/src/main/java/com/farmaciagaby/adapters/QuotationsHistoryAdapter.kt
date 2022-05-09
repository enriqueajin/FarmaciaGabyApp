package com.farmaciagaby.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmaciagaby.R
import com.farmaciagaby.databinding.QuotationItemBinding
import com.farmaciagaby.models.Quotation

class QuotationsHistoryAdapter(private val quotationList: List<Quotation>) :
    RecyclerView.Adapter<QuotationsHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = QuotationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(quotationList[position])
    }

    override fun getItemCount(): Int {
        return quotationList.size
    }

    class ViewHolder(itemView: QuotationItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        private val binding = itemView

        fun setData(quotation: Quotation) {
            binding.tvSupplierName.text = quotation.supplierName
            binding.tvDateValue.text = quotation.date
        }
    }
}
