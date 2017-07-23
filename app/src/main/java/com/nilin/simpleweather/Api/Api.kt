package com.nilin.retrofit2_rxjava2_demo

import com.nilin.simpleweather.model.Weather
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by liangd on 2017/7/11.
 */
interface Api {

    @GET("{city}")
    fun getData(@Path("city") type: String): Observable<Weather>



    companion object Factory{
        fun create():Api {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://apicloud.mob.com/v1/weather/query?key=appkey&city=")
                    .build()

            return retrofit.create(Api::class.java)
        }
    }
}
