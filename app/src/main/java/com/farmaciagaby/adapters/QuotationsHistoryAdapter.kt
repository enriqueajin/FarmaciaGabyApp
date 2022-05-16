package com.farmaciagaby.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmaciagaby.R
import com.farmaciagaby.databinding.QuotationItemBinding
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.models.Quotation
import java.text.SimpleDateFormat
import java.util.*

class QuotationsHistoryAdapter(private val quotationList: List<Detalle>) :
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

        fun setData(quotation: Detalle) {
            binding.tvSupplierName.text = quotation.proveedor
            binding.tvDateValue.text = formatDate(quotation.fecha.toDate())
        }

        private fun formatDate(date: Date): String {
            return SimpleDateFormat("dd-MM-yyyy").format(date)
        }
    }
}
