package com.farmaciagaby.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmaciagaby.databinding.QuotationItemBinding
import com.farmaciagaby.models.Detalle
import java.text.SimpleDateFormat
import java.util.*

class QuotationsHistoryAdapter(
    private var quotationList: MutableList<Detalle>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<QuotationsHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = QuotationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quotation = quotationList[position]
        holder.setData(quotation)

        holder.binding.quotationItemContainer.setOnClickListener { view ->
            onClickListener.onClickListener(quotation, view)
        }
    }

    override fun getItemCount(): Int {
        return quotationList.size
    }

    fun deleteQuotation(quotation: Detalle) {
        quotationList.remove(quotation)
    }

    class ViewHolder(itemView: QuotationItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val binding = itemView

        fun setData(quotation: Detalle) {
            binding.tvSupplierName.text = quotation.proveedor
            binding.tvDateValue.text = formatDate(quotation.fecha.toDate())
        }

        private fun formatDate(date: Date): String {
            return SimpleDateFormat("dd-MM-yyyy").format(date)
        }
    }

    class OnClickListener(
        val clickListener: (product: Detalle, view: View) -> Unit,
    ) {
        fun onClickListener(product: Detalle, view: View) = clickListener(product, view)
    }
}
