package com.farmaciagaby.fragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.SimpleStringAdapter
import com.farmaciagaby.databinding.FragmentQuotationDetailsBinding
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.viewmodels.QuotationsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class QuotationDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentQuotationDetailsBinding
    private val gson = GsonBuilder().create()
    private val args: QuotationDetailsFragmentArgs by navArgs()
    private val viewModel: QuotationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuotationDetailsBinding.inflate(inflater, container, false)
        setData()
        Log.d("TAG", "onCreateView: creating view")
        return binding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun setData() {
        // Set title to the action bar
        setActionBarTitle(resources.getString(R.string.action_bar_quotation_details))

        val quotation = gson.fromJson(args.quotation, Detalle::class.java)

        // set supplier and date
        binding.tvSupplier.text = quotation.proveedor
        binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy").format(quotation.fecha.toDate())
        binding.tvDescription.text = quotation.descripcion

        // Set up quotation preview adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rvProducts.setHasFixedSize(true)
        binding.rvProducts.layoutManager = layoutManager
        binding.rvProducts.isNestedScrollingEnabled = false

        // Build an ArrayList of type String to pass as argument in SimpleStringAdapter
        val productList = arrayListOf<String>()
        val items = quotation.productos.single() as ArrayList<String>
        for (item in items) {
            productList.add(item)
        }

        val adapter = SimpleStringAdapter(productList)
        binding.rvProducts.adapter = adapter

        binding.btnShare.setOnClickListener {
            if (checkPermission()) {
                // Hide the buttons while take the screenshot
                binding.btnShare.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE

                val uriString = saveImage(binding.quotationDetailsContainer)

                // Show button after take the screenshot
                binding.btnShare.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE

                val uri = Uri.parse(uriString)
                shareImage(uri)
            } else {
                requestPermission(false, activityResult)
            }
        }

        binding.btnDelete.setOnClickListener { view ->
            showDeleteDialog(quotation, view)
        }
    }

    private fun showDeleteDialog(quotation: Detalle, view: View) {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Eliminar producto")
            .setMessage("¿Está seguro que desea eliminar este producto?")
            .setPositiveButton("Eliminar") { dialogInterface, i ->
                showLoadingDialog()

                viewLifecycleOwner.lifecycleScope.launch {

                    viewModel.getQuotationDocumentReference(quotation)
                    viewModel.quotationDocRef.observe(requireActivity()) { documentReference ->
                        hideLoadingDialog()

                        // Perform the delete statement
                        viewModel.deleteQuotation(documentReference)
                        Navigation.findNavController(view).popBackStack()
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialogInterface, i -> }
            .show()
    }

    private val activityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Log.d(
                        "TAG",
                        "storageActivityResultLauncher: manage extenal storage permission is granted"
                    )
                    if (result.resultCode == STORAGE_PERMISSION_CODE) {
                        saveImage(binding.quotationDetailsContainer)
                    }
                } else {
                    Log.d(
                        "TAG",
                        "storageActivityResultLauncher: manage extenal storage permission is denied"
                    )
                }
            }
        }
    )
}