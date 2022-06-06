package com.farmaciagaby.models.response

import com.farmaciagaby.models.Employee

data class EmployeeResponse(
    var employee: Employee = Employee(),
    var employeeId: String = ""
)