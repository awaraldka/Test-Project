package com.swipetest.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.swipetest.Repositry.SwipeTestRepository
import com.swipetest.Utils.NetworkHelper
import com.swipetest.Utils.Resource
import com.swipetest.api.Constants
import com.swipetest.api.response.AddProductResponse
import com.swipetest.api.response.PojoClass
import com.swipetest.api.response.ProductListResponse
import com.swipetest.api.response.Products
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(app: Application, private val repo: SwipeTestRepository, private val networkHelper: NetworkHelper) : AndroidViewModel(app){


    private val listCategoryData: MutableStateFlow<Resource<ArrayList<Products>>> = MutableStateFlow(Resource.Empty())
    val _listCategoryData: StateFlow<Resource<ArrayList<Products>>> = listCategoryData

    private val addProductData: MutableStateFlow<Resource<AddProductResponse>> = MutableStateFlow(Resource.Empty())
    val _addProductData: StateFlow<Resource<AddProductResponse>> = addProductData





    //     listCategoryApi

    fun listCategoryApi() = viewModelScope.launch {
        listCategoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getProductList()
                .catch {e ->
                    listCategoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    listCategoryData.value = lisCategoryResponseHandle(data)
                }
        }else{
            listCategoryData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun lisCategoryResponseHandle(response: Response<ArrayList<Products>>): Resource<ArrayList<Products>> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }



    //     add Product Api

    fun addProductApi(product_name:RequestBody, product_type:RequestBody, price:RequestBody, tax: RequestBody, files: MultipartBody.Part) = viewModelScope.launch {
        addProductData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addProductApi(product_name, product_type, price, tax, files)
                .catch {e ->
                    addProductData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addProductData.value = addProductResponseHandle(data)
                }
        }else{
            addProductData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun addProductResponseHandle(response: Response<AddProductResponse>): Resource<AddProductResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }



}