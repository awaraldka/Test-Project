package com.swipetest.di


import android.content.Context
import com.swipetest.api.Api_Interface
import com.swipetest.api.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SwipeModule {

    @Provides
    @Singleton
    fun provideMyApi(@ApplicationContext context: Context): Api_Interface {
        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()

            .callTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)




                if (response.code == 440) {
//                    val responseBody = response.body.string()
//                    val json = JSONObject(responseBody)
//                    val message = json.getString("responseMessage")

                }

                response
            })
            .build()

        val retrofitInstance by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api_Interface::class.java)
        }
        return retrofitInstance
    }


}
