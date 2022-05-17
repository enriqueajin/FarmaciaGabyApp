package com.farmaciagaby.fragments

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.SimpleStringAdapter
import com.farmaciagaby.databinding.FragmentRequestQuotationPreviewBinding
import com.farmaciagaby.models.Product
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*

class RequestQuotationPreviewFragment : BaseFragment() {

    private lateinit var binding: FragmentRequestQuotationPreviewBinding
    private val gson = GsonBuilder().create()
    private val args: RequestQuotationPreviewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestQuotationPreviewBinding.inflate(inflater, container, false)
        setData()
        return binding.root
    }

    private fun setData() {
        // Show action bar and set title
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.action_bar_quotation_preview)

        val productList = (gson.fromJson(args.productList, Array<Product>::class.java)).toList()
        val mappedProductList = productList.map { product -> product.nombre } as ArrayList<String>

        // set supplier and date
        binding.tvSupplier.text = args.supplier
        binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy").format(Date())

        // Set up quotation preview adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rvProducts.setHasFixedSize(true)
        binding.rvProducts.layoutManager = layoutManager
        binding.rvProducts.isNestedScrollingEnabled = false

        val adapter = SimpleStringAdapter(mappedProductList)
        binding.rvProducts.adapter = adapter

        binding.btnContinue.setOnClickListener { view ->
            if (checkPermission()) {
                Log.d("TAG", "permission already granted")
                val uri = saveImage(binding)
                val action = uri?.let { stringUri ->
                    RequestQuotationPreviewFragmentDirections.actionQuotationPreviewToSuccessfulQuotation(
                        stringUri
                    )
                }
                if (action != null) {
                    Navigation.findNavController(view).navigate(action)
                }
            } else {
                Log.d("TAG", "Permission still not granted. requesting...")
                requestPermission(true, activityResult)
            }
        }
    }

    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Log.d("TAG", "storageActivityResultLauncher: manage extenal storage permission is granted")
                if (result.resultCode == STORAGE_PERMISSION_CODE) {
                    saveImage(binding)
                }
            } else {
                Log.d("TAG", "storageActivityResultLauncher: manage extenal storage permission is denied")
            }
        }
    })

//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.isNotEmpty()) {
//                // Check if permission is granted
//                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
//                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
//
//                if (write && read) {
//                    saveImage(binding)
//                    Log.d("TAG", "onRequestPermissionsResult: image saved")
//                }
//            }
//        }
//    }
}