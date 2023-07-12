package com.swipetest.api.response

import com.google.gson.annotations.SerializedName


data class ProductListResponse(
    @SerializedName("JSON") val data: List<Products>
)


data class Products(
    @SerializedName("image") val image: String,
    @SerializedName("price") val price: Number,
    @SerializedName("product_name")val product_name: String,
    @SerializedName("product_type") val product_type: String,
    @SerializedName("tax") val tax: Number
)