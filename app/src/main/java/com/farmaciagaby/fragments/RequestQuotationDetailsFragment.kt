package com.farmaciagaby.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgs
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentRequestQuotationDetailsBinding
import com.farmaciagaby.models.Product
import com.google.gson.GsonBuilder


class RequestQuotationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRequestQuotationDetailsBinding
    private val gson = GsonBuilder().create()
    private val args: RequestQuotationDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestQuotationDetailsBinding.inflate(inflater, container, false)
        setData()
        return binding.root
    }

    private fun setData() {
        // Show action bar and set title
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.action_bar_request_quotation)

        // Get arguments
        val productList = (gson.fromJson(args.productList, Array<Product>::class.java)).toList();

        for (product in productList) {
            Log.d("TAG", "producto: ${product.nombre}")
        }

        // Set up suppliers spinner data
        val suppliers = arrayOf("Promeco", "Bendición y Fe", "Droguería La Esperanza", "Jogral")
        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_supplier_item,
               suppliers
            )

        binding.spinnerSuppliers.setAdapter(adapter)
        binding.spinnerSuppliers.requestFocus()

        binding.btnContinue.setOnClickListener { view ->
            val action = RequestQuotationDetailsFragmentDirections.actionQuotationDetailsToQuotationPreview(args.productList)
            Navigation.findNavController(view).navigate(action)
        }
    }
}