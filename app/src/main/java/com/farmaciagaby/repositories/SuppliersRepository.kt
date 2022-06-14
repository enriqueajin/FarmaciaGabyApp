package com.farmaciagaby.repositories

import androidx.lifecycle.MutableLiveData
import com.farmaciagaby.models.Supplier
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object SuppliersRepository {

    fun getAllSuppliers(): MutableLiveData<MutableList<Supplier>> {
        val suppliers = MutableLiveData<MutableList<Supplier>>()
        val db = Firebase.firestore
        db.collection("proveedor").orderBy("nombre", Query.Direction.ASCENDING).get().addOnSuccessListener { result ->
            val listData = mutableListOf<Supplier>()
            for (document in result) {
                val name = document.getString("nombre")
                val supplier = Supplier(name!!)
                listData.add(supplier)
            }
            suppliers.value = listData
        }
        return suppliers
    }
}