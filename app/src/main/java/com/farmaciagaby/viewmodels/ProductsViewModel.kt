package com.farmaciagaby.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farmaciagaby.models.Product
import com.farmaciagaby.repositories.ProductsRepository
import com.google.firebase.firestore.DocumentReference

class ProductsViewModel: ViewModel() {

    lateinit var productsData: MutableLiveData<MutableList<Product>>
    lateinit var productDocRef: MutableLiveData<DocumentReference>

    fun getAllProducts(): MutableLiveData<MutableList<Product>> {
        productsData = MutableLiveData<MutableList<Product>>()
        ProductsRepository.getAllProducts().observeForever {
            productsData.value = it
        }
        return productsData
    }

    fun getProductDocumentReference(productName: String): MutableLiveData<DocumentReference> {
        productDocRef = MutableLiveData<DocumentReference>()
        ProductsRepository.getProductDocumentReference(productName).observeForever {
            productDocRef.value = it
        }
        return productDocRef
    }

    fun addProduct(product: Product) {
        ProductsRepository.addProduct(product)
    }

    fun updateProduct(docRef: DocumentReference, newProductName: String) {
        ProductsRepository.updateProduct(docRef, newProductName)
    }

    fun deleteProduct(docRef: DocumentReference) {
        ProductsRepository.deleteProduct(docRef)
    }
}