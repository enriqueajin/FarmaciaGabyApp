package com.farmaciagaby.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.farmaciagaby.models.Detalle
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object QuotationsRepository {

    fun getAllQuotation() : MutableLiveData<MutableList<Detalle>> {
        val quotations = MutableLiveData<MutableList<Detalle>>()
        val db = Firebase.firestore
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
            quotations.value = listData
        }
        return quotations
    }

    fun addQuotation(quotation: Detalle): MutableLiveData<String> {
        val db = Firebase.firestore
        val data = MutableLiveData<String>()
        db.collection("cotizacion").add(quotation).addOnSuccessListener { docRef ->
            Log.d("TAG", "addQuotation: ${docRef.id}")
            data.value = docRef.id
        }
        Log.d("TAG", "id from repository: ${data.value}")
        return data
    }

    fun getQuotationDocumentReference(quotation: Detalle): MutableLiveData<DocumentReference?> {
        Log.d("TAG", "Quotation: $quotation")
        val db = Firebase.firestore
        var id = ""
        val docRef = MutableLiveData<DocumentReference?>()

        db.collection("cotizacion")
            .whereEqualTo("descripcion", quotation.descripcion)
            .whereEqualTo("empleado", quotation.empleado)
            .whereEqualTo("fecha", quotation.fecha)
            .whereEqualTo("proveedor", quotation.proveedor)
            .get()
        .addOnSuccessListener { result ->
            id = result.documents[0].id
            docRef.value = db.collection("cotizacion").document(id)
        }
        .addOnFailureListener {
            it.printStackTrace()
        }
        return docRef
    }

    fun deleteQuotation(documentReference: DocumentReference) {
        documentReference.delete()
    }
}