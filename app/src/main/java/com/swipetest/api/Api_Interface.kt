package com.swipetest.api


import com.swipetest.api.response.AddProductResponse
import com.swipetest.api.response.ProductListResponse
import com.swipetest.api.response.Products
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface Api_Interface {


    @GET("public/get")
    suspend fun getProductList(): Response<ArrayList<Products>>


    @Multipart
    @POST("public/add")
    suspend fun addProductApi(@Part("product_name")product_name:RequestBody,
                              @Part("product_type")product_type:RequestBody,
                              @Part("price")price:RequestBody,
                              @Part("tax")tax:RequestBody,@Part files:MultipartBody.Part): Response<AddProductResponse>


}








