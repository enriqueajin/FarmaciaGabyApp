package com.farmaciagaby.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.SelectQuotationProductsAdapter
import com.farmaciagaby.databinding.FragmentRequestQuotationBinding
import com.farmaciagaby.models.Product

class RequestQuotationFragment : Fragment() {

    private lateinit var binding: FragmentRequestQuotationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestQuotationBinding.inflate(inflater, container, false)
        setData()

        return binding.root
    }

    private fun setData() {
        binding.rvQuotationProducts.layoutManager = LinearLayoutManager(context);

        // Fake data
        val productList = listOf(
            Product("Tabcin"),
            Product("Pañal de adulto"),
            Product("Jeringa"),
            Product("Penicilina"),
            Product("Amoxicilina"),
            Product("Aspirina"),
            Product("Agua oxigenada"),
            Product("Alka Seltzer"),
            Product("Crema humectante"),
            Product("Agua Misclear"),
            Product("Protector Solar"),
            Product("Toallitas húmedas")
        )

        val adapter = SelectQuotationProductsAdapter(productList)
        binding.rvQuotationProducts.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.general_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_product -> {
                // Open add product dialog
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}