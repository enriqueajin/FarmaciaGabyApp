package com.farmaciagaby.network.api

import com.farmaciagaby.models.Product
import com.farmaciagaby.models.request.CreateProductRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApi {

    @GET("productos")
    fun getAllProducts() : Call<List<Product>>

    @POST("producto")
    fun createProduct(@Body request: CreateProductRequest) : Call<Product>
}