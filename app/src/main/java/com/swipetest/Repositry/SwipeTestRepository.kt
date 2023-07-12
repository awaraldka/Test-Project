package com.swipetest.Repositry



import com.swipetest.api.Interface_Implement
import com.swipetest.api.response.AddProductResponse
import com.swipetest.api.response.ProductListResponse
import com.swipetest.api.response.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class SwipeTestRepository @Inject constructor(private val apiServiceImpl: Interface_Implement) {


    fun getProductList(): Flow<Response<ArrayList<Products>>> = flow {
        emit(apiServiceImpl.getProductList())
    }.flowOn(Dispatchers.IO)

    fun addProductApi(product_name: RequestBody, product_type:RequestBody, price:RequestBody, tax:RequestBody, files: MultipartBody.Part): Flow<Response<AddProductResponse>> = flow {
        emit(apiServiceImpl.addProductApi(product_name, product_type, price, tax, files))
    }.flowOn(Dispatchers.IO)


}