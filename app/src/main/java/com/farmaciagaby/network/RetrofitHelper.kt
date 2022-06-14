package com.farmaciagaby.network

import com.farmaciagaby.utils.StringUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper {

    companion object {

        private lateinit var retrofit: Retrofit

        fun <S> createService(serviceClass: Class<S>): S {
            retrofit = Retrofit.Builder()
                .baseUrl(StringUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(3, TimeUnit.MINUTES)
                        .connectTimeout(3, TimeUnit.MINUTES)
                        .callTimeout(3, TimeUnit.MINUTES)
                        .writeTimeout(3, TimeUnit.MINUTES)
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build()
                )
                .build()

            return retrofit.create(serviceClass)
        }
    }
}