package com.farmaciagaby.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmaciagaby.models.response.EmployeeResponse
import com.farmaciagaby.repositories.EmployeesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeesViewModel: ViewModel() {

    var employeeData = MutableLiveData<EmployeeResponse>()

    fun getEmployeeByUid(uid: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            val employee = EmployeesRepository.getEmployeeByUid(uid)
            employeeData.postValue(employee)
        }
    }
}