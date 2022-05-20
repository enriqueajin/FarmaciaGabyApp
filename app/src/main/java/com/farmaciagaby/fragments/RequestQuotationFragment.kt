package com.farmaciagaby.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.CheckProductsAdapter
import com.farmaciagaby.databinding.FragmentRequestQuotationBinding
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.models.Product
import com.farmaciagaby.viewmodels.ProductViewModel
import com.farmaciagaby.viewmodels.ProductsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import java.util.*

class RequestQuotationFragment : BaseFragment() {

    private lateinit var binding: FragmentRequestQuotationBinding
    private lateinit var adapter: CheckProductsAdapter
    private val gson = GsonBuilder().create()
    private val productViewModel: ProductViewModel by viewModels()
    private val viewModel: ProductsViewModel by viewModels()

    private var mProductList = mutableListOf<Product>()
    private val mSearchList = mutableListOf<Product>()

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
        // Show action bar and set title
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.action_bar_request_quotation)

        // Set up the select quotation products adapter
        binding.rvQuotationProducts.layoutManager = LinearLayoutManager(context);

        // Get all products from Firestore
        viewModel.getAllProducts().observe(requireActivity(), Observer { productsList ->
            mProductList = productsList
            adapter = CheckProductsAdapter(mProductList)
            binding.rvQuotationProducts.adapter = adapter
        })

        // Fake data
//        val productList = arrayListOf(
//            Product("Tabcin"),
//            Product("Pañal de adulto"),
//            Product("Jeringa"),
//            Product("Penicilina"),
//            Product("Amoxicilina"),
//            Product("Aspirina"),
//            Product("Agua oxigenada"),
//            Product("Alka Seltzer"),
//            Product("Crema humectante"),
//            Product("Agua Misclear"),
//            Product("Protector Solar"),
//            Product("Toallitas húmedas"),
//            Product("Talco de bebé"),
//            Product("Peptobismol"),
//            Product("Vicks"),
//            Product("Cofal"),
//            Product("Pastilla de teñir"),
//        )
//
//        adapter = CheckProductsAdapter(productList)
//        binding.rvQuotationProducts.adapter = adapter

        binding.btnContinue.setOnClickListener { view ->
            for (product in adapter.getCheckedProducts()) {
                Log.d("TAG", "producto: " + product.nombre)
            }
            Log.d("TAG", "--------------------------------------------")

            val argument = gson.toJson(adapter.getCheckedProducts())
            val action = RequestQuotationFragmentDirections.actionPassProductListToRequestQuotationDetails(argument)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.general_menu, menu)

        val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = "Escriba el nombre del producto..."
        searchView.setBackgroundColor(resources.getColor(R.color.white))
        searchView.callOnClick()
        searchView.performClick()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                Log.d("TAG", "onQueryTextChange: ${validate(p0!!)}")
                filter(p0)
                return false
            }
        })
    }

    private fun filter(text: String) {
        val filteredProducts = mutableListOf<Product>()

        for (product in mProductList) {
            val productName = product.nombre.lowercase(Locale.getDefault())
            val text = text.lowercase(Locale.getDefault())

            if (productName.contains(text)) {
                filteredProducts.add(product)
            }
        }

        if (filteredProducts.isNotEmpty()) {
            adapter.filterList(filteredProducts)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_product -> {
                showBottomDialog()
                true
            }
            R.id.action_search -> {
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showBottomDialog() {
        val dialog = context?.let { BottomSheetDialog(it, R.style.BottomSheetDialogTheme) }
        val view = layoutInflater.inflate(R.layout.add_product_dialog, null)

        dialog?.setContentView(view)
        dialog?.show()

        val input = dialog?.findViewById<TextInputEditText>(R.id.et_product_name)
        input?.requestFocus()

        val button = dialog?.findViewById<MaterialButton>(R.id.btnAdd)
        button?.setOnClickListener {
            val product = Product(input?.text.toString().trim())
            adapter.addNewProduct(product)
            viewModel.addProduct(product)
//            addProductToDatabase(product.name.trim())
            dialog.dismiss()
        }
    }

    /**
     * Insert a product to the database
     * @param productName String with the product name
     */
        private fun addProductToDatabase(productName: String) {
        productViewModel.createProduct(productName)
        productViewModel.productLiveData.observe(this, Observer { product ->

        })
    }
}