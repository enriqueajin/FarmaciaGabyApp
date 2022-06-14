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
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentSuccessfulRequestQuotationBinding
import com.farmaciagaby.models.Detalle
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat

class SuccessfulRequestQuotationFragment : BaseFragment() {

    private lateinit var binding: FragmentSuccessfulRequestQuotationBinding
    private val args: SuccessfulRequestQuotationFragmentArgs by navArgs()
    private lateinit var uri: Uri
    private val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccessfulRequestQuotationBinding.inflate(inflater, container, false)
        setData()
        return binding.root
    }

    private fun setData() {
        // Set title to the action bar
        setActionBarTitle(resources.getString(R.string.action_bar_quotation_successful))

        // Get arguments
        uri = Uri.parse(args.uriString)
        val quotation = gson.fromJson(args.quotation, Detalle::class.java)
        val quotationId = args.quotationId

        val quotationDate = SimpleDateFormat("dd-MM-yyyy").format(quotation.fecha.toDate())
        val quotationTime = SimpleDateFormat("HH:mm:ss").format(quotation.fecha.toDate())

        // Set TextViews information
        binding.tvTransactionNumber.text = quotationId
        binding.tvDateValue.text = quotationDate
        binding.tvTimeValue.text = quotationTime

        binding.btnShare.setOnClickListener { view ->
            if (checkPermission()) {
                Log.d("TAG", "FROM SUCCESSFUL permission already granted")
                shareImage(uri)
            } else {
                Log.d("TAG", "FROM SUCCESSFUL Permission still not granted. requesting...")
                requestPermission(false, activityResult)
            }
        }

        binding.btnFinalize.setOnClickListener { view ->
            Navigation.findNavController(view).popBackStack()
        }
    }

    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Log.d("TAG", "storageActivityResultLauncher: FROM SUCCESSFUL manage extenal storage permission is granted")
                if (result.resultCode == SHARE_FILES_PERMISSION_CODE) {
                    shareImage(uri)
                }
            } else {
                Log.d("TAG", "storageActivityResultLauncher: FROM SUCCESSFUL manage extenal storage permission is denied")
            }
        }
    })
}