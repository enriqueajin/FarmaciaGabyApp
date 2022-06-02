package com.farmaciagaby.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.repositories.QuotationsRepository
import com.google.firebase.firestore.DocumentReference

class QuotationsViewModel: ViewModel() {

    lateinit var quotationIdData: MutableLiveData<String>
    lateinit var quotationDocRef: MutableLiveData<DocumentReference>
    lateinit var allQuotationsData: MutableLiveData<MutableList<Detalle>>

    fun getAllQuotation(): MutableLiveData<MutableList<Detalle>> {
        allQuotationsData = MutableLiveData<MutableList<Detalle>>()
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

    fun getQuotationDocumentReference(quotation: Detalle): MutableLiveData<DocumentReference> {
        quotationDocRef = MutableLiveData<DocumentReference>()
        QuotationsRepository.getQuotationDocumentReference(quotation).observeForever {
            quotationDocRef.value = it
        }
        return quotationDocRef
    }

    fun deleteQuotation(docRef: DocumentReference) {
        QuotationsRepository.deleteQuotation(docRef)
    }

}