package com.nilin.simpleweather.Api

import com.nilin.simpleweather.model.NetData
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by liangd on 2017/7/11.
 */
interface Api {
    //    @GET("s6/weather/forecast?key=0c93bf67d3f944669ae99fe715fde101")
    @GET("s6/weather/{weather-type}?key=0c93bf67d3f944669ae99fe715fde101")
//    fun getData(@Query("location") city: String): Observable<NetData>
    fun getData(@Path("weather-type") weather_type: String, @Query("location") location: String): Observable<NetData>

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
                    .baseUrl("https://free-api.heweather.net/")
                    .build()
            return retrofit.create(Api::class.java)
        }
    }
}
