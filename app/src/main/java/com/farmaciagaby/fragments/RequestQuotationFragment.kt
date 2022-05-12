package com.farmaciagaby.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.CheckProductsAdapter
import com.farmaciagaby.databinding.FragmentRequestQuotationBinding
import com.farmaciagaby.models.Product
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder

class RequestQuotationFragment : Fragment() {

    private lateinit var binding: FragmentRequestQuotationBinding
    private lateinit var adapter: CheckProductsAdapter
    private val gson = GsonBuilder().create()

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

        // Fake data
        val productList = arrayListOf(
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
            Product("Toallitas húmedas"),
            Product("Talco de bebé"),
            Product("Peptobismol"),
            Product("Vicks"),
            Product("Cofal"),
            Product("Pastilla de teñir"),
        )

        adapter = CheckProductsAdapter(productList)
        binding.rvQuotationProducts.adapter = adapter

        binding.btnContinue.setOnClickListener { view ->
            for (product in adapter.getCheckedProducts()) {
                Log.d("TAG", "producto: " + product.name)
            }
            Log.d("TAG", "--------------------------------------------")

            val argument = gson.toJson(adapter.getCheckedProducts())
            val action = RequestQuotationFragmentDirections.actionPassProductListToRequestQuotationDetails(argument)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.general_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_product -> {
                showBottomDialog()
                true
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
            dialog.dismiss()
        }
    }
}