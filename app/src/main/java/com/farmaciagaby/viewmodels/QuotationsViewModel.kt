package com.farmaciagaby.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.repositories.QuotationsRepository
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuotationsViewModel: ViewModel() {

    var quotationIdData = MutableLiveData<String>()
    var quotationDocRef = MutableLiveData<DocumentReference>()
    var allQuotationsData = MutableLiveData<MutableList<Detalle>>()

    fun getAllQuotation() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = QuotationsRepository.getAllQuotation()
            Log.d("TAG", "getAllQuotation: observing result from view model")

            if (!result.isNullOrEmpty()) {
                allQuotationsData.postValue(result)
            }
        }
    }

    fun addQuotation(quotation: Detalle) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = QuotationsRepository.addQuotation(quotation)
            quotationIdData.postValue(result)
            Log.d("TAG", "id from viewmodel: ${quotationIdData.value}")
        }
    }

    fun getQuotationDocumentReference(quotation: Detalle) {
        viewModelScope.launch(Dispatchers.Main) {
           val result =  QuotationsRepository.getQuotationDocumentReference(quotation)

            if (result != null) {
                quotationDocRef.postValue(result!!)
            }
        }
    }

    fun deleteQuotation(docRef: DocumentReference) {
        viewModelScope.launch(Dispatchers.Main) {
            QuotationsRepository.deleteQuotation(docRef)
        }
    }

}