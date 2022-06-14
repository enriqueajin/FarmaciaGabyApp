package com.farmaciagaby.adapters

import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmaciagaby.databinding.SimpleStringLayoutBinding

class SimpleStringAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<SimpleStringAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SimpleStringLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: SimpleStringLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {

        val binding = itemView

        fun setData(item: String) {
            binding.tvItem.text = item
        }
    }
}