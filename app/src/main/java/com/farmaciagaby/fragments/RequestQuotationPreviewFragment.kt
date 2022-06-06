package com.farmaciagaby.fragments

import android.content.Context
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.SimpleStringAdapter
import com.farmaciagaby.databinding.FragmentRequestQuotationPreviewBinding
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.models.Product
import com.farmaciagaby.viewmodels.EmployeesViewModel
import com.farmaciagaby.viewmodels.QuotationsViewModel
import com.google.firebase.Timestamp
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.text.SimpleDateFormat
import java.util.*

class RequestQuotationPreviewFragment : BaseFragment() {

    private lateinit var binding: FragmentRequestQuotationPreviewBinding
    private val gson = GsonBuilder().create()
    private val args: RequestQuotationPreviewFragmentArgs by navArgs()
    private val quotationsViewModel: QuotationsViewModel by viewModels()
    private val employeesViewModel: EmployeesViewModel by viewModels()

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
                val sharedPref = activity?.getSharedPreferences(
                    resources.getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )
                val userUid = sharedPref?.getString(
                    getString(R.string.uid_key),
                    "uid"
                )
                Log.d("TAG", "user uid from preview: $userUid")

                showLoadingDialog()

                viewLifecycleOwner.lifecycleScope.launch {

                    // Insert quotation in Firestore database
                    val description = args.description
                    val date = Timestamp(Date())
                    val supplier = args.supplier

                    // Get user (employee) by uid using coroutines}
                    employeesViewModel.getEmployeeByUid(userUid)
                    employeesViewModel.employeeData.observe(requireActivity()) { employeeResponse ->
                        val employeeId = employeeResponse.employeeId

                        val quotation = Detalle(description, date, employeeId, supplier, mappedProductList)
                        var quotationId: String?

                        // Hide the button while take the screenshot
                        binding.btnContinue.visibility = View.GONE

                        // Save quotation as JPG image
                        val uri = saveImage(binding.voucherContainer)

                        // Show button after take the screenshot
                        binding.btnContinue.visibility = View.VISIBLE

                        // Add quotation using coroutines
                        quotationsViewModel.addQuotation(quotation)
                        quotationsViewModel.quotationIdData.observe(requireActivity()) { id ->
                            hideLoadingDialog()
                            quotationId = id
                            Log.d("TAG", "id from view: ${quotationId}")

                            val action =
                                RequestQuotationPreviewFragmentDirections.actionQuotationPreviewToSuccessfulQuotation(
                                    uriString = uri!!,
                                    quotation = gson.toJson(quotation),
                                    quotationId = id
                                )
                            Navigation.findNavController(view).navigate(action)
                        }
                    }
                }

            } else {
                Log.d("TAG", "Permission still not granted. requesting...")
                requestPermission(true, activityResult)
            }
        }
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
                        saveImage(binding.voucherContainer)
                    }
                } else {
                    Log.d(
                        "TAG",
                        "storageActivityResultLauncher: manage extenal storage permission is denied"
                    )
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