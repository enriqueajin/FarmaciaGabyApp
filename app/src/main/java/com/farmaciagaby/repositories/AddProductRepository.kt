package com.farmaciagaby.repositories

import androidx.lifecycle.MutableLiveData
import com.farmaciagaby.models.Product
import com.farmaciagaby.models.request.CreateProductRequest
import com.farmaciagaby.network.RetrofitHelper
import com.farmaciagaby.network.api.ProductApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AddProductRepository {

    private var productApi: ProductApi = RetrofitHelper.createService(ProductApi::class.java)

    fun createProduct(productName: String): MutableLiveData<Product> {
        val productData = MutableLiveData<Product>()
        val request = CreateProductRequest(productName)

        productApi.createProduct(request).enqueue(object : Callback<Product?> {
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                if (response.isSuccessful) {
                    productData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                // Nothing happens
            }
        })
        return productData
    }
}