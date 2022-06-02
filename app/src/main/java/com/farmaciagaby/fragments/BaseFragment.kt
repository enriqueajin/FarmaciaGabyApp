package com.farmaciagaby.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentQuotationDetailsBinding
import com.farmaciagaby.databinding.FragmentRequestQuotationPreviewBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

const val STORAGE_PERMISSION_CODE = 100         // Request code for write and read storage permission
const val SHARE_FILES_PERMISSION_CODE = 200     // Request code for share files permission

/**
 * Custom base fragment that provides methods which can be used in any children fragment.
 */
open class BaseFragment : Fragment() {

    private lateinit var linearProgressIndicator: LinearProgressIndicator

    fun requestPermission(isSave: Boolean, activityResultLauncher: ActivityResultLauncher<Intent>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 (R) or above
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                activityResultLauncher.launch(intent)
            } catch (ex: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                activityResultLauncher.launch(intent)
            }
        } else {
            // Android below 11 (R)
            if (isSave) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    STORAGE_PERMISSION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    SHARE_FILES_PERMISSION_CODE
                )
            }
        }
    }

    fun shareImage(uri: Uri) {
        try {
            checkForFolder()

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"

            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun saveImage(v: View): String? {
        val view = v as ViewGroup

        try {
            checkForFolder()

            // Naming file with format 'Cotización 11-05-2022 12:15 PM'
            val fileName = "Cotización " + SimpleDateFormat("dd-MM-yyyy HH:mm:ss aaa").format(Calendar.getInstance().time)

            val uri = MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                getBitmapFromView(
                    view,
                    view.getChildAt(0).height
                ),
                fileName,
                ""
            )

            requireContext().sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse(uri)
                )
            )
            return uri

        } catch (e: Exception) {
            e.printStackTrace()

            return null
        }
    }

    fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 (R) or above
            return Environment.isExternalStorageManager()
        } else {
            // Android below 11 (R)
            val write = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val read = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getBitmapFromView(view: View, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val background = view.background

        if (background == null) {
            background?.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }

        view.draw(canvas)
        return bitmap
    }

    private fun checkForFolder() {
        val path = Environment.getExternalStorageDirectory().toString()
        val file = File(path, "pictures")
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    fun validateEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(email)
        return matcher.find()
    }

    fun validate(value: String) : Boolean {
        return value.trim().isNotEmpty()
    }

    fun showProgressIndicator() {
        linearProgressIndicator = requireActivity().findViewById(R.id.linear_progress_indicator)
        linearProgressIndicator.visibility = View.VISIBLE
    }

    fun hideProgressIndicator() {
        linearProgressIndicator.visibility = View.GONE
    }

    fun showKeyboard(view: View) {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getActionBar(): ActionBar {
        return (activity as AppCompatActivity).supportActionBar!!
    }

    fun setActionBarTitle(title: String) {
        getActionBar().title = title
    }

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
//                    saveImage(binding.voucherContainer)
//                    Log.d("TAG", "onRequestPermissionsResult: image saved")
//                }
//            }
//        }
//    }
}