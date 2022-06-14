package com.farmaciagaby.repositories

import android.util.Log
import com.farmaciagaby.models.Product
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object ProductsRepository {

    /**
     * Get all products in the database
     * @return mutable list of [Product]
     */
    suspend fun getAllProducts(): MutableList<Product> {
        return withContext(Dispatchers.IO) {
            val products = mutableListOf<Product>()
            val db = Firebase.firestore
            db.collection("producto").orderBy("nombre").get().addOnSuccessListener { result ->
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    products.add(product)
                }
            }.await()
            products
        }
    }

    /**
     * Add a new product to the database
     * @param [Product]
     */
    suspend fun addProduct(product: Product) {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore
            db.collection("producto").add(product).addOnSuccessListener { }.await()
        }
    }

    /**
     * Get a product document reference
     * @param productName (String)
     * @return [DocumentReference]
     */
    suspend fun getProductDocumentReference(productName: String): DocumentReference? {
        return withContext(Dispatchers.IO) {
            Log.d("TAG", "Product name is: $productName")
            val db = Firebase.firestore
            var id = ""
            var docRef: DocumentReference? = null

            db.collection("producto").whereEqualTo("nombre", productName).get().addOnSuccessListener { result ->
                id = result.documents.single().id
                docRef = db.collection("producto").document(id)

            }.addOnFailureListener {
                it.printStackTrace()
            }.await()
            docRef
        }
    }

    /**
     * Update an existing product from a document reference
     * @param [DocumentReference]
     * @param newProductName (String)
     */
    suspend fun updateProduct(docRef: DocumentReference, newProductName: String) {
        return withContext(Dispatchers.IO) {
            docRef.update("nombre", newProductName).await()
        }
    }

    /**
     * Delete an existing product from a document reference
     * @param [DocumentReference]
     */
    suspend fun deleteProduct(docRef: DocumentReference) {
        return withContext(Dispatchers.IO) {
            docRef.delete().await()
        }
    }
}