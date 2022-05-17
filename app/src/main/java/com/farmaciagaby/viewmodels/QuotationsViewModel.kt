package com.farmaciagaby.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.repositories.QuotationsRepository

class QuotationsViewModel: ViewModel() {

//    lateinit var allQuotationsData: MutableLiveData<MutableList<Detalle>>
//    val repo = QuotationsRepository

    fun getAllQuotation(): MutableLiveData<MutableList<Detalle>>{
        val allQuotationsData = MutableLiveData<MutableList<Detalle>>()
        QuotationsRepository.getAllQuotation().observeForever {
            allQuotationsData.value = it
        }
        return allQuotationsData
    }
}