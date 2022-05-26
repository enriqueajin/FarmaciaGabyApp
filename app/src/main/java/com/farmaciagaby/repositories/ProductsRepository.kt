package com.farmaciagaby.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.farmaciagaby.models.Product
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
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

    fun getProductDocumentReference(productName: String): MutableLiveData<DocumentReference?> {
        Log.d("TAG", "Product name is: $productName")
        val db = Firebase.firestore
        var id = ""
        val docRef = MutableLiveData<DocumentReference?>()

        db.collection("producto").whereEqualTo("nombre", productName).get().addOnSuccessListener { result ->
            id = result.documents[0].id
            docRef.value = db.collection("producto").document(id)

        }.addOnFailureListener {
            it.printStackTrace()
        }
        return docRef
    }

    fun updateProduct(docRef: DocumentReference, newProductName: String) {
        val db = Firebase.firestore
        docRef.update("nombre", newProductName)
    }
}