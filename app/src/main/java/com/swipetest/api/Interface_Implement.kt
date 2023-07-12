package com.swipetest.api

import com.swipetest.api.Api_Interface
import com.swipetest.api.response.AddProductResponse
import com.swipetest.api.response.ProductListResponse
import com.swipetest.api.response.Products
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


class Interface_Implement @Inject constructor(private val apiService: Api_Interface) {


    suspend fun getProductList(): Response<ArrayList<Products>> = apiService.getProductList()

    suspend fun addProductApi(product_name: RequestBody, product_type:RequestBody, price:RequestBody, tax:RequestBody, files: MultipartBody.Part
    ): Response<AddProductResponse> = apiService.addProductApi(product_name, product_type, price, tax, files)

}

