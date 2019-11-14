package com.nilin.simpleweather.Api

import com.nilin.simpleweather.model.NetData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by liangd on 2017/7/11.
 */
interface Api {

    @GET("s6/weather/{weather-type}?key=0c93bf67d3f944669ae99fe715fde101")
    fun getData(@Path("weather-type") weather_type: String, @Query("location") location: String): Call<NetData>
}
