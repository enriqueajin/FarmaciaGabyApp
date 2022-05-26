package com.farmaciagaby.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.ManageProductsAdapter
import com.farmaciagaby.databinding.FragmentManageProductsBinding
import com.farmaciagaby.models.Product
import com.farmaciagaby.viewmodels.ProductsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ManageProductsFragment : BaseFragment() {

    private lateinit var binding: FragmentManageProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var mAdapter: ManageProductsAdapter
    private var mProductList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageProductsBinding.inflate(inflater, container, false)
        setData()
        return binding.root
    }

    private fun setData() {
        // Set up the manage products adapter
        binding.rvManageProducts.layoutManager = LinearLayoutManager(context)

        // Get all products from Firestore
        viewModel.getAllProducts().observe(requireActivity(), Observer { productList ->
            mProductList = productList
            mAdapter = ManageProductsAdapter(
                mProductList,
                ManageProductsAdapter.OnClickListener({ product ->
                    run {
                        showDeleteDialog(product)
                    }
                }) { product ->
                    run {
                        showUpdateDialog(product)
                    }
                })
            binding.rvManageProducts.adapter = mAdapter
        })
    }

    private fun showDeleteDialog(product: Product) {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Eliminar producto")
            .setMessage("¿Está seguro que desea eliminar este producto?")
            .setPositiveButton("Eliminar") { dialogInterface, i ->
                viewModel.getProductDocumentReference(product.nombre)
                    .observe(requireActivity(), Observer { documentReference ->
                        viewModel.deleteProduct(documentReference)
                    })

                mAdapter.deleteProduct(product)
            }
            .setNegativeButton("Cancelar") { dialogInterface, i -> }
            .show()
    }

    private fun showUpdateDialog(currentProduct: Product) {
        val dialog = context?.let { BottomSheetDialog(it, R.style.BottomSheetDialogTheme) }
        val view = layoutInflater.inflate(R.layout.add_product_dialog, null)

        dialog?.setContentView(view)
        dialog?.show()

        // Get layout components
        val advice = dialog?.findViewById<TextView>(R.id.tv_advice_type_product)
        val label = dialog?.findViewById<TextView>(R.id.tv_add_product)
        val input = dialog?.findViewById<TextInputEditText>(R.id.et_product_name)
        val button = dialog?.findViewById<MaterialButton>(R.id.btnAdd)

        label?.text = resources.getString(R.string.add_product_update_product)
        input?.text = Editable.Factory.getInstance().newEditable(currentProduct.nombre)
        input?.requestFocus()

        button?.setOnClickListener {
            val newProductName = input?.text.toString()

            if (validate(newProductName)) {
                viewModel.getProductDocumentReference(currentProduct.nombre)
                    .observe(requireActivity(), Observer { documentReference ->
                        viewModel.updateProduct(documentReference, newProductName)
                    })

                // Update the product name in the RecyclerView
                mAdapter.updateProduct(currentProduct, newProductName)

                dialog.dismiss()
            } else {
                // Show the type product name TextView if input is empty
                advice?.visibility = View.VISIBLE
            }
        }

        // Hide the type product name TextView if input is not empty
        input?.addTextChangedListener {
            val newProductName = input.text.toString()

            if (validate(newProductName)) {
                advice?.visibility = View.GONE
            }
        }
    }
}