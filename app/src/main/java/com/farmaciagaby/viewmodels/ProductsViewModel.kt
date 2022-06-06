package com.farmaciagaby.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmaciagaby.models.Product
import com.farmaciagaby.repositories.ProductsRepository
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel: ViewModel() {

    lateinit var productDocRef: MutableLiveData<DocumentReference>  // Initialized every time is used to avoid hold old values
    var productsData = MutableLiveData<MutableList<Product>>()

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = ProductsRepository.getAllProducts()
            if (!result.isNullOrEmpty()) {
                productsData.postValue(result)
                Log.d("TAG", "getAllProducts: posting values")
            } else {
                Log.d("TAG", "getAllProducts: result is empty")
            }
        }
    }

    fun getProductDocumentReference(productName: String) {
        productDocRef = MutableLiveData<DocumentReference>()
        viewModelScope.launch(Dispatchers.Main) {
            val result = ProductsRepository.getProductDocumentReference(productName)

            if (result != null) {
                productDocRef.postValue(result!!)
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.Main) {
            ProductsRepository.addProduct(product)
        }
    }

    fun updateProduct(docRef: DocumentReference, newProductName: String) {
        viewModelScope.launch(Dispatchers.Main) {
            ProductsRepository.updateProduct(docRef, newProductName)
        }
    }

    fun deleteProduct(docRef: DocumentReference) {
        viewModelScope.launch(Dispatchers.Main) {
            ProductsRepository.deleteProduct(docRef)
        }
    }
}