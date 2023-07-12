package com.swipetest

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class SwipeApp : Application(){

    lateinit var context: Context


    override fun onCreate() {
        super.onCreate()
        context = applicationContext


    }

}