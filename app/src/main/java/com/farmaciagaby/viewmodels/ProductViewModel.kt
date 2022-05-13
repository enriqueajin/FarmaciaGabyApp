package com.farmaciagaby.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farmaciagaby.models.Product
import com.farmaciagaby.repositories.AddProductRepository

class ProductViewModel: ViewModel() {

    lateinit var productLiveData: MutableLiveData<Product>

    fun createProduct(productName: String) {
        productLiveData = AddProductRepository.createProduct(productName)
    }
}