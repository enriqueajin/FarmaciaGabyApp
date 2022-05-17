package com.farmaciagaby.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.farmaciagaby.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ProductsRepository {

    fun getAllProducts(): MutableLiveData<MutableList<Product>> {
        val products = MutableLiveData<MutableList<Product>>()
        val db = Firebase.firestore
        db.collection("producto").orderBy("nombre").get().addOnSuccessListener { result ->
            val listData = mutableListOf<Product>()
            for (document in result) {
                val name = document.getString("nombre")

                val product = Product(name!!)
                listData.add(product)
            }
            products.value = listData
        }
        return products
    }

    fun addProduct(product: Product) {
        val db = Firebase.firestore
        db.collection("producto").add(product).addOnSuccessListener { }
    }
}