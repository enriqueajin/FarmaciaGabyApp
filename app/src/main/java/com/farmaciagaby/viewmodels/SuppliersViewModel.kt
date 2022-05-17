package com.farmaciagaby.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farmaciagaby.models.Supplier
import com.farmaciagaby.repositories.SuppliersRepository

class SuppliersViewModel : ViewModel() {

    lateinit var suppliersData: MutableLiveData<MutableList<Supplier>>

    fun getAllSuppliers(): MutableLiveData<MutableList<Supplier>> {
        suppliersData = MutableLiveData<MutableList<Supplier>>()
        SuppliersRepository.getAllSuppliers().observeForever {
            suppliersData.value = it
        }
        return suppliersData
    }
}