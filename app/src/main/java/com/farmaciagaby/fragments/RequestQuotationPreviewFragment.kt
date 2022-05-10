package com.farmaciagaby.fragments

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.adapters.SimpleStringAdapter
import com.farmaciagaby.databinding.FragmentRequestQuotationPreviewBinding
import com.farmaciagaby.models.Product
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileOutputStream
import java.util.*

class RequestQuotationPreviewFragment : Fragment() {

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
        val productList = (gson.fromJson(args.productList, Array<Product>::class.java)).toList()
        val mappedProductList = productList.map { product -> product.name } as ArrayList<String>

        // Set up quotation preview adapter
        binding.rvProducts.layoutManager = LinearLayoutManager(context);

        val adapter = SimpleStringAdapter(mappedProductList)
        binding.rvProducts.adapter = adapter

        binding.btnContinue.setOnClickListener {
            saveImage()
        }
    }

    private fun checkPermissions(isSave: Boolean) {
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (java.lang.Boolean.TRUE == isSave) {

            }
        }
    }

    private fun saveImage() {
        val bitmap = getBitmapFromView(
            binding.svPreview,
            binding.svPreview.getChildAt(0).width,
            binding.svPreview.getChildAt(0).height
        )

        try {
            val defaultFile = File(Environment.getExternalStorageDirectory().absolutePath + "/com.farmaciagaby")
            if (!defaultFile.exists()) {
                defaultFile.mkdirs()

                val fileName = "Cotización " + Calendar.getInstance() + ".jpg"
                var file = File(defaultFile, fileName)

                if (file.exists()) {
                    file.delete()
                    file = File(defaultFile, fileName)
                }

                val output = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                output.flush()
                output.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getBitmapFromView(view: View, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
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
}