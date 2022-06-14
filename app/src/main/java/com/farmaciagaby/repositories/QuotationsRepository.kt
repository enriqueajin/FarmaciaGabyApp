package com.farmaciagaby.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.farmaciagaby.models.Detalle
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object QuotationsRepository {

    /**
     * Get all existing quotations from the database
     * @return mutable list of [Detalle]
     */
    suspend fun getAllQuotation(): MutableList<Detalle> {
        return withContext(Dispatchers.IO) {
            var quotations = mutableListOf<Detalle>()

            val db = Firebase.firestore
            val dbReference = Firebase.firestore.collection("users")
            db.collection("cotizacion").orderBy("fecha", Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
                val listData = mutableListOf<Detalle>()
                for (document in result) {
                    val description = document.getString("descripcion")
                    val employeeId = document.getString("empleado")
                    val date = document.getDate("fecha")
                    val supplier = document.getString("proveedor")
                    val products = listOf(document.get("productos"))

                    val detail = Detalle(description!!, Timestamp(date!!), employeeId!!, supplier!!, products)
                    listData.add(detail)
                }
                quotations = listData
                Log.d("TAG", "getAllQuotation: observing from repository $quotations")
            }.await()
            quotations
        }
    }

    /**
     * Insert a new quotation to the database.
     * @param [Detalle]
     * @return document id inserted
     */
    suspend fun addQuotation(quotation: Detalle): String {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore
            var data = ""
            db.collection("cotizacion").add(quotation).addOnSuccessListener { docRef ->
                Log.d("TAG", "addQuotation: ${docRef.id}")
                data = docRef.id
            }.await()
            Log.d("TAG", "id from repository: ${data}")
            data
        }
    }

    /**
     * Get a quotation document reference
     * @param [Detalle]
     * @return quotation document reference
     */
    suspend fun getQuotationDocumentReference(quotation: Detalle): DocumentReference? {
        return withContext(Dispatchers.IO) {
            Log.d("TAG", "Quotation: $quotation")
            val db = Firebase.firestore
            var id = ""
            var docRef: DocumentReference? = null

            db.collection("cotizacion")
                .whereEqualTo("descripcion", quotation.descripcion)
                .whereEqualTo("empleado", quotation.empleado)
                .whereEqualTo("fecha", quotation.fecha)
                .whereEqualTo("proveedor", quotation.proveedor)
                .get()
                .addOnSuccessListener { result ->
                    id = result.documents.single().id
                    docRef = db.collection("cotizacion").document(id)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }.await()
            docRef
        }
    }

    /**
     * Delete a existing quotation from a document reference
     * @param [DocumentReference]
     */
    suspend fun deleteQuotation(documentReference: DocumentReference) {
        return withContext(Dispatchers.IO) {
            documentReference.delete().await()
        }
    }
}