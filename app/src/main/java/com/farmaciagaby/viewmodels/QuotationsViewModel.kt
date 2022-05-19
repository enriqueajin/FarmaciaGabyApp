package com.farmaciagaby.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.repositories.QuotationsRepository

class QuotationsViewModel: ViewModel() {

//    lateinit var allQuotationsData: MutableLiveData<MutableList<Detalle>>
//    val repo = QuotationsRepository
    lateinit var quotationIdData: MutableLiveData<String>

    fun getAllQuotation(): MutableLiveData<MutableList<Detalle>> {
        val allQuotationsData = MutableLiveData<MutableList<Detalle>>()
        QuotationsRepository.getAllQuotation().observeForever {
            allQuotationsData.value = it
        }
        return allQuotationsData
    }

    fun addQuotation(quotation: Detalle): MutableLiveData<String> {
        quotationIdData = MutableLiveData<String>()
        QuotationsRepository.addQuotation(quotation).observeForever {
            quotationIdData.value = it
        }
        Log.d("TAG", "id from viewmodel: ${quotationIdData.value}")
        return quotationIdData
    }
}