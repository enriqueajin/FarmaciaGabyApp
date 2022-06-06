package com.farmaciagaby.repositories

import android.util.Log
import com.farmaciagaby.models.Employee
import com.farmaciagaby.models.response.EmployeeResponse
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object EmployeesRepository {

    /**
     * Get an employee by the uid firebase authentication
     * @param uid (String)
     * @return [Employee]
     */
    suspend fun getEmployeeByUid(uid: String?): EmployeeResponse {
        return withContext(Dispatchers.IO) {
            var employeeResponse = EmployeeResponse()
            val db = Firebase.firestore

            db.collection("empleado").whereEqualTo("uid", uid).get().addOnSuccessListener { result ->
                val item = result.single()
                val employeeId = item.id
                val employee = item.toObject(Employee::class.java)
                employeeResponse = EmployeeResponse(employee, employeeId)
                Log.d("TAG", "getAllQuotation: observing employee response from repository $employeeResponse")
            }.await()
            employeeResponse
        }
    }
}
