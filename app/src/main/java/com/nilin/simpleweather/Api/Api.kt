package com.nilin.retrofit2_rxjava2_demo

import android.util.Log
import com.nilin.simpleweather.model.Weather
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by liangd on 2017/7/11.
 */
interface Api {
    @GET("v1/weather/query?key=1d47f9f5e9be8")
    fun getData(@Query("city") city: String): Observable<Weather>

    companion object Factory {
        fun create(): Api {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://apicloud.mob.com/")
                    .build()
            return retrofit.create(Api::class.java)
        }
    }
}
