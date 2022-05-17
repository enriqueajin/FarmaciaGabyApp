package com.farmaciagaby.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farmaciagaby.models.Product
import com.farmaciagaby.repositories.ProductsRepository

class ProductsViewModel: ViewModel() {

    lateinit var productsData: MutableLiveData<MutableList<Product>>

    fun getAllProducts(): MutableLiveData<MutableList<Product>> {
        productsData = MutableLiveData<MutableList<Product>>()
        ProductsRepository.getAllProducts().observeForever {
            productsData.value = it
        }
        return productsData
    }

    fun addProduct(product: Product) {
        ProductsRepository.addProduct(product)
    }
}