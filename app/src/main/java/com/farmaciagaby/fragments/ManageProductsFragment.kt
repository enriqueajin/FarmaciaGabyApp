package com.farmaciagaby.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.ManageProductsAdapter
import com.farmaciagaby.databinding.FragmentManageProductsBinding
import com.farmaciagaby.models.Product
import com.farmaciagaby.viewmodels.ProductsViewModel

class ManageProductsFragment : Fragment() {

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
                        Log.d("TAG", "setData: i'm deleting adapter with product $product")
                    }
                }) { product ->
                    run {
                        Log.d("TAG", "setData: i'm editing adapter with product $product")
                    }
                })
            binding.rvManageProducts.adapter = mAdapter
        })
    }
}