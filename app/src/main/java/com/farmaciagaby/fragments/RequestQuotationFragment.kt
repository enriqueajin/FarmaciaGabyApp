package com.farmaciagaby.fragments

import android.app.SearchManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.CheckProductsAdapter
import com.farmaciagaby.databinding.FragmentRequestQuotationBinding
import com.farmaciagaby.models.Product
import com.farmaciagaby.viewmodels.ProductViewModel
import com.farmaciagaby.viewmodels.ProductsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import java.util.*

class RequestQuotationFragment : BaseFragment() {

    private lateinit var binding: FragmentRequestQuotationBinding
    private lateinit var adapter: CheckProductsAdapter
    private val gson = GsonBuilder().create()
    private val productViewModel: ProductViewModel by viewModels()
    private val viewModel: ProductsViewModel by viewModels()

    private var isSearchViewOpen = false                        // The Flag that handle if SearchView is open
    private lateinit var addItem: MenuItem                      // Get the add icon in action bar
    private lateinit var searchItem: MenuItem                   // Get the search icon in action bar
    private lateinit var searchView: SearchView                 // Set up a SearchView in search item action

    private var mProductList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        showLoadingDialog()

        // Get all products from Firestore
        viewModel.getAllProducts().observe(requireActivity(), Observer { productsList ->
            mProductList = productsList
            adapter = CheckProductsAdapter(mProductList)
            binding.rvQuotationProducts.adapter = adapter
            hideLoadingDialog()
        })

        binding.btnContinue.setOnClickListener { view ->
            // Validate that checked products list is not empty
            if (adapter.getCheckedProducts().isNotEmpty()) {
                for (product in adapter.getCheckedProducts()) {
                    Log.d("TAG", "producto: " + product.nombre)
                }
                Log.d("TAG", "--------------------------------------------")

                val argument = gson.toJson(adapter.getCheckedProducts())
                val action = RequestQuotationFragmentDirections.actionPassProductListToRequestQuotationDetails(argument)
                Navigation.findNavController(view).navigate(action)
            } else {
                Snackbar.make(
                    binding.requestQuotationContainer,
                    "Debe seleccionar al menos un producto para continuar",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_product -> {
                showBottomDialog()
                true
            }
            R.id.action_search -> {
                // Set search view as opened and hide the add item icon from the action bar
                isSearchViewOpen = true
                addItem.isVisible = false

                // Change action bar color to white when searching
                getActionBar().setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))

                // Expand the SearchView just after the user click on the search item icon
                searchItem.expandActionView()
                searchView.requestFocus()
                showKeyboard(searchView)
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.general_menu, menu)

        // Get menu items
        addItem = menu.findItem(R.id.action_add_product)
        searchItem = menu.findItem(R.id.action_search)

        // Set up search functionality when user click on menu search item
        val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = "Escriba el producto..."
        searchView.setBackgroundColor(resources.getColor(R.color.white))
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextFocusChangeListener { view, isFocused ->
            if (!isFocused) {
                // Change action bar color to white when searching
                getActionBar().setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue_primary)))

                // Clear text in SearchView when it lose focus
                searchView.setQuery("", false)

                // Show the add item icon when SearchView lose focus and update elements in the RecyclerView
                hideKeyboard(searchView)
                addItem.isVisible = true
                adapter.filterList(mProductList)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                filter(query!!)
                return false
            }
        })
    }

    private fun filter(text: String) {
        val filteredProducts = mutableListOf<Product>()

        for (product in mProductList) {
            val productName = product.nombre.lowercase(Locale.getDefault())
            val query = text.lowercase(Locale.getDefault())

            if (productName.contains(query)) {
                filteredProducts.add(product)
            }
        }

        if (filteredProducts.isNotEmpty()) {
            adapter.filterList(filteredProducts)
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
            adapter.addProduct(product)

            showLoadingDialog()
            viewModel.addProduct(product)
            hideLoadingDialog()

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