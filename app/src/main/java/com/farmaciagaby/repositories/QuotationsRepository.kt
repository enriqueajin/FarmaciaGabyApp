package com.farmaciagaby.repositories

import androidx.lifecycle.MutableLiveData
import com.farmaciagaby.models.Detalle
import com.google.firebase.Timestamp
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

    fun addQuotation(quotation: Detalle): String? {
        val db = Firebase.firestore
        var quotationId: String? = null
        db.collection("cotizacion").add(quotation).addOnSuccessListener {
            quotationId = it.id
        }
        return quotationId
    }
}