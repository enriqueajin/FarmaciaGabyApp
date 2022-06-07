package com.farmaciagaby.fragments

import android.app.SearchManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
import java.util.*

class ManageProductsFragment : BaseFragment() {

    private lateinit var binding: FragmentManageProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var mAdapter: ManageProductsAdapter
    private var mProductList = mutableListOf<Product>()

    private var isSearchViewOpen = false                        // The Flag that handle if SearchView is open
    private lateinit var addItem: MenuItem                      // Get the add icon in action bar
    private lateinit var searchItem: MenuItem                   // Get the search icon in action bar
    private lateinit var searchView: SearchView                 // Set up a SearchView in search item action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        // Set title to the action bar
        setActionBarTitle(resources.getString(R.string.action_bar_manage_products))

        // Set up the manage products adapter
        binding.rvManageProducts.layoutManager = LinearLayoutManager(context)

        showLoadingDialog()

        // Get all products from Firestore using coroutines
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getAllProducts()
            viewModel.productsData.observe(requireActivity()) { productList ->
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
                hideLoadingDialog()
            }
        }
    }

    private fun showDeleteDialog(product: Product) {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Eliminar producto")
            .setMessage("¿Está seguro que desea eliminar este producto?")
            .setPositiveButton("Eliminar") { dialogInterface, i ->
                showLoadingDialog()

                // Delete product using coroutines
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getProductDocumentReference(product.nombre)
                    viewModel.productDocRef.observe(requireActivity()) { documentReference ->
                        viewModel.deleteProduct(documentReference)
                        hideLoadingDialog()
                    }
                }

                mProductList.remove(product)
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
        button?.text = resources.getString(R.string.button_update)

        label?.text = resources.getString(R.string.add_product_update_product)
        input?.text = Editable.Factory.getInstance().newEditable(currentProduct.nombre)
        input?.requestFocus()

        button?.setOnClickListener {
            val newProductName = input?.text.toString()

            if (validate(newProductName)) {
                showLoadingDialog()

                // Update product using coroutines
                viewLifecycleOwner.lifecycleScope.launch {

                    viewModel.getProductDocumentReference(currentProduct.nombre)
                    viewModel.productDocRef.observe(requireActivity()) { documentReference ->
                        viewModel.updateProduct(documentReference, newProductName)
                        hideLoadingDialog()
                    }
                }

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
                mAdapter.filterList(mProductList)
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
            mAdapter.filterList(filteredProducts)
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
            mAdapter.addProduct(product)

            showLoadingDialog()
            viewModel.addProduct(product)
            hideLoadingDialog()

            dialog.dismiss()
        }
    }
}