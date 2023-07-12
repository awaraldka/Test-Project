package com.swipetest.api.response

import com.google.gson.annotations.SerializedName

class AddProductResponse(
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean,

    )