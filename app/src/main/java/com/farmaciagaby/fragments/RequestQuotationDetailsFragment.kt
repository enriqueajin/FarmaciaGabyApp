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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentRequestQuotationDetailsBinding
import com.farmaciagaby.models.Product
import com.farmaciagaby.viewmodels.SuppliersViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder


class RequestQuotationDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentRequestQuotationDetailsBinding
    private val gson = GsonBuilder().create()
    private val args: RequestQuotationDetailsFragmentArgs by navArgs()
    private val viewModel: SuppliersViewModel by viewModels()

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

        // Get suppliers from Firestore
        viewModel.getAllSuppliers().observe(requireActivity(), Observer { supplierList ->
            val supplierNames = mutableListOf<String>()
            for (supplier in supplierList) {
                supplierNames.add(supplier.nombre)
            }
            val suppliersArray = supplierNames.toTypedArray()
            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_supplier_item, suppliersArray)
            binding.spinnerSuppliers.setAdapter(adapter)
        })

        // Set up suppliers spinner data
//        val suppliers = arrayOf("Promeco", "Bendición y Fe", "Droguería La Esperanza", "Jogral")
//        val adapter =
//            ArrayAdapter(
//                requireContext(),
//                R.layout.dropdown_menu_supplier_item,
//               suppliers
//            )
//
//        binding.spinnerSuppliers.setAdapter(adapter)
//        binding.spinnerSuppliers.requestFocus()

        binding.btnContinue.setOnClickListener { view ->
            val description = binding.etDescription.text.toString()
            val supplier = binding.spinnerSuppliers.text.toString()

            // Validate fields
            if (validate(supplier)) {
                // Passing product list, description and supplier as arguments
                val action = RequestQuotationDetailsFragmentDirections.actionQuotationDetailsToQuotationPreview(args.productList, description, supplier)
                Navigation.findNavController(view).navigate(action)
            } else {
                Snackbar.make(
                    binding.fragmentDetailsContainer,
                    "Por favor seleccione un proveedor.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}