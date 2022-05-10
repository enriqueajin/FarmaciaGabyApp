package com.farmaciagaby.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.farmaciagaby.R
import com.farmaciagaby.databinding.FragmentSuccessfulRequestQuotationBinding
import com.farmaciagaby.models.Product
import com.google.gson.GsonBuilder

class SuccessfulRequestQuotationFragment : Fragment() {

    private lateinit var binding: FragmentSuccessfulRequestQuotationBinding
    private val args: SuccessfulRequestQuotationFragmentArgs by navArgs()
    private val gson = GsonBuilder().create()
    private val productList = (gson.fromJson(args.productList, Array<Product>::class.java)).toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccessfulRequestQuotationBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setData() {

    }

    private fun createRequestQuotationImage() {

    }
}