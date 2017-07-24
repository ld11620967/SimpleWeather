package com.nilin.simpleweather.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast

import com.nilin.retrofit2_rxjava2_demo.Api
import com.nilin.simpleweather.R
import com.nilin.simpleweather.model.Weather
import com.nilin.simpleweather.utils.ActivityCollector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

import java.text.ParseException
import java.text.SimpleDateFormat


class MainActivity : Activity() {
    private var mytoast: Toast? = null
    private var mExitTime: Long = 0
    var isExit = false
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipe_refresh.setColorSchemeResources(R.color.colorPrimary)
        initView()
        val intent = intent
        ActivityCollector.addActivity(this)
        swipe_refresh.isRefreshing = true

        val a = intent.getIntExtra("isRefreshing", 0)
        if (a == 0) {
            swipe_refresh.isRefreshing = true
            var pref = getSharedPreferences("settings_pref", Context.MODE_PRIVATE)
            var city = pref.getString("city", "")
            getWeatherData(city)

        } else if (a == 1) {
            swipe_refresh.isRefreshing = true
            isUpdataWeather()
        }

        swipe_refresh.setOnRefreshListener { isUpdataWeather() }
    }

    fun initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }
        settings_imv.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    protected fun getWeatherData(city: String) {
        var api = Api.Factory.create()

        api.getData(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    weather ->
                    parseResult(weather)
                }, {
                    _ ->
                    //loadError()
                    //loadFinish()
                })
    }

    fun parseResult(weather: Weather) {
        if (weather.msg) {
            DisplayToast("获取最新天气失败")
        } else {
            val editor = getSharedPreferences("weather_pref", Context.MODE_PRIVATE).edit()
            editor.putString("city", weather!!.result[0].city)
            editor.putString("updata_time", weather!!.result[0].updateTime)
            editor.putString("temperature", weather!!.result[0].temperature)
            editor.putString("now_info", weather!!.result[0].weather)

            for (future in weather!!.result[0].future) {
                i = i + 1
                try {
                    val editor = getSharedPreferences("weather_pref", Context.MODE_PRIVATE).edit()
                    editor.putString("date$i", weather!!.result[0].future[i].week)
                    editor.putString("forecast_info$i", weather!!.result[0].future[i].dayTime)
                    editor.putString("temperature", weather!!.result[0].temperature)

                    var temperature_max=weather!!.result[0].future[i].temperature.substring(0,2)
                    var temperature_min=weather!!.result[0].future[i].temperature.substring(7)

//                    Log.i("temperature_min",temperature_min)
                    editor.putInt("temperature_max$i", temperature_max.toInt())
                    editor.putInt("temperature_min$i", temperature_min.toInt())
                    editor.commit()
                } catch (e: Exception) {
                    e.printStackTrace();

                }
            }

            editor.commit()
            changeWeatherView()
        }
        swipe_refresh.isRefreshing = false
    }

    fun changeWeatherView() {
        var pref = getSharedPreferences("weather_pref", Context.MODE_PRIVATE)
        var city1 = pref.getString("city", "")
        var updata_time1 = pref.getString("updata_time", "")
        var temperature1 = pref.getString("temperature", "")
        var now_info = pref.getString("now_info", "")

        var temperature_max = pref.getString("temperature_max1", "")
        var temperature_min = pref.getString("temperature_min1", "")

        Log.i("temperature_max",temperature_max)
        Log.i("temperature_min",temperature_min)



        if (now_info == "晴") {
            weather_now!!.setBackgroundResource(R.drawable.weather_sunny_bg)
        } else if (now_info.contains("云")) {
            weather_now!!.setBackgroundResource(R.drawable.weather_cloudy_bg)
        } else if (now_info == "阴") {
            weather_now!!.setBackgroundResource(R.drawable.weather_overcast_bg)
        } else if (now_info.contains("雨")) {
            weather_now!!.setBackgroundResource(R.drawable.weather_rain_bg)
        } else if (now_info.contains("雪")) {
            weather_now!!.setBackgroundResource(R.drawable.weather_snow_bg)
        } else {
            weather_now!!.setBackgroundResource(R.drawable.weather_foggy_bg)
        }

        city.text = city1
        var updata_time2 = updata_time1!!.substring(4, 12)
        val sb = StringBuilder(updata_time2)
        sb.insert(2, "-")
        sb.insert(5, " ")
        sb.insert(8, ":")
        updata_time.text = sb.toString()
        temperature.text = temperature1.substring(0, temperature1.length - 1)
        info.text = now_info


        val date3 = pref.getString("date3", "")
        val date4 = pref.getString("date4", "")
        val date5 = pref.getString("date5", "")
        val date6 = pref.getString("date6", "")
        val date7 = pref.getString("date7", "")
        val date8 = pref.getString("date8", "")
        val date9 = pref.getString("date9", "")
        val date10 = pref.getString("date10", "")
        val forecast_info1 = pref.getString("forecast_info1", "")
        val forecast_info2 = pref.getString("forecast_info2", "")
        val forecast_info3 = pref.getString("forecast_info3", "")
        val forecast_info4 = pref.getString("forecast_info4", "")
        val forecast_info5 = pref.getString("forecast_info5", "")
        val forecast_info6 = pref.getString("forecast_info6", "")
        val forecast_info7 = pref.getString("forecast_info7", "")
        val forecast_info8 = pref.getString("forecast_info8", "")
        val forecast_info9 = pref.getString("forecast_info9", "")
        val forecast_info10 = pref.getString("forecast_info10", "")

        weather3.text = date3
        weather4.text = date4
        weather5.text = date5
        weather6.text = date6
        weather7.text = date7
        weather8.text = date8
        weather9.text = date9
        weather10.text = date10

        weather_info1.text = forecast_info1
        weather_info2.text = forecast_info2
        weather_info3.text = forecast_info3
        weather_info4.text = forecast_info4
        weather_info5.text = forecast_info5
        weather_info6.text = forecast_info6
        weather_info7.text = forecast_info7
        weather_info8.text = forecast_info8
        weather_info9.text = forecast_info9
        weather_info10.text = forecast_info10

        if (forecast_info1 == "晴") {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info1!!.contains("云")) {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info1 == "阴") {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info1 == "小雨") {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info1 == "中雨") {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info1.contains("雷雨")) {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info1.contains("阵雨")) {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info1.contains("雨")) {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info1.contains("雪")) {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon1!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info2 == "晴") {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info1!!.contains("云")) {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info2 == "阴") {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info2 == "小雨") {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info2 == "中雨") {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info2.contains("雷雨")) {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info2.contains("阵雨")) {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info2.contains("雨")) {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info2.contains("雪")) {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon2!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info3 == "晴") {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info3!!.contains("云")) {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info3 == "阴") {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info3 == "小雨") {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info3 == "中雨") {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info3.contains("雷雨")) {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info3.contains("阵雨")) {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info3.contains("雨")) {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info3.contains("雪")) {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon3!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info4 == "晴") {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info4!!.contains("云")) {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info4 == "阴") {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info4 == "小雨") {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info4 == "中雨") {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info4.contains("雷雨")) {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info4.contains("阵雨")) {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info4.contains("雨")) {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info4.contains("雪")) {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon4!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info5 == "晴") {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info5!!.contains("云")) {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info5 == "阴") {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info5 == "小雨") {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info5 == "中雨") {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info5.contains("雷雨")) {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info5.contains("阵雨")) {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info5.contains("雨")) {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info5.contains("雪")) {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon5!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info6 == "晴") {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info6!!.contains("云")) {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info6 == "阴") {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info6 == "小雨") {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info6 == "中雨") {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info6.contains("雷雨")) {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info6.contains("阵雨")) {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info6.contains("雨")) {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info6.contains("雪")) {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon6!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info7 == "晴") {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info7!!.contains("云")) {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info7 == "阴") {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info7 == "小雨") {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info7 == "中雨") {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info7.contains("雷雨")) {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info7.contains("阵雨")) {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info7.contains("雨")) {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info7.contains("雪")) {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon7!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info8 == "晴") {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info8!!.contains("云")) {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info8 == "阴") {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info8 == "小雨") {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info8 == "中雨") {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info8.contains("雷雨")) {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info8.contains("阵雨")) {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info8.contains("雨")) {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info8.contains("雪")) {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon8!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info9 == "晴") {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info9!!.contains("云")) {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info9 == "阴") {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info9 == "小雨") {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info9 == "中雨") {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info9.contains("雷雨")) {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info9.contains("阵雨")) {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info9.contains("雨")) {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info9.contains("雪")) {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon9!!.setBackgroundResource(R.drawable.weather_fog)
        }


        if (forecast_info10 == "晴") {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info10!!.contains("云")) {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info10 == "阴") {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info10 == "小雨") {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info10 == "中雨") {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info10.contains("雷雨")) {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info10.contains("阵雨")) {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_shower)
        } else if (forecast_info10.contains("雨")) {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info10.contains("雪")) {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon10!!.setBackgroundResource(R.drawable.weather_fog)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                DisplayToast("再按一次退出程序")
                mExitTime = System.currentTimeMillis()
            } else {
                mytoast!!.cancel()
                if (!isExit) {
                    isExit = true
                    ActivityCollector.finishAll()
                } else {
                    isExit = false
                    ActivityCollector.finishAll()
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun DisplayToast(str: String) {
        if (mytoast == null) {
            mytoast = Toast.makeText(this@MainActivity, str, Toast.LENGTH_SHORT)
        } else {
            mytoast!!.setText(str)
        }
        mytoast!!.show()
    }

    fun isUpdataWeather() {
        val updata_time_pref = getSharedPreferences("weather_pref", Context.MODE_PRIVATE)
        val updata_time = updata_time_pref.getString("updata_time", "")
        //将字符串转为日期
        val sdf = SimpleDateFormat("yyyyMMddHHmmsss")
        var date: java.util.Date? = null
        try {
            date = sdf.parse(updata_time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val s1 = date!!.time//将时间转为毫秒
        val s2 = System.currentTimeMillis()//得到当前的毫秒
        val minutes_passed = (s2 - s1) / 1000 / 60
        if (minutes_passed > 60) {
            val pref = getSharedPreferences("settings_pref", Context.MODE_PRIVATE)
            val city = pref.getString("city", "")
            getWeatherData(city)
        } else {
            changeWeatherView()
            DisplayToast("已是最新天气")
        }

        swipe_refresh.isRefreshing = false
    }

    companion object {

        private val TAG = "test"
    }


    //    fun getWeatherData() {
//        val pref = getSharedPreferences("settings_pref", Context.MODE_PRIVATE)
//        val city = pref.getString("city", "")
//        val weatherUrl = "http://apicloud.mob.com/v1/weather/query?key=1d47f9f5e9be8&city=$city"
//        HttpUtil.sendOkHttpRequest(weatherUrl, object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    DisplayToast("获取最新天气失败")
//                    swipe_refresh.isRefreshing = false
//                }
//            }
//
//            @Throws(IOException::class)
//            override fun onResponse(call: Call, response: Response) {
//                val json = response.body()!!.string()
//                var jsonObject: JSONObject? = null
//                val editor = getSharedPreferences("weather_pref", Context.MODE_PRIVATE).edit()
//                var i = 0
//                try {
//                    jsonObject = JSONObject(json)
//                    val jsonArray = jsonObject!!.getJSONArray()
//                    val weatherContent = jsonArray.getJSONObject(0).toString()
//                    val api = Gson().fromJson(weatherContent, Api::class.java)
//                    if ("no more requests" != api!!.msg) {
//                        editor.putString("city", weather!!.basic.city)
//                        editor.putString("updata_time", weather!!.basic.update.loc)
//                        editor.putString("temperature", weather!!.now.temperature)
//                        editor.putString("now_info", weather!!.now.more.info)


//                        for (forecast in weather!!.forecastList) {
//                            i = i + 1
//                            val format = SimpleDateFormat("yyyy-MM-dd")
//                            val c = Calendar.getInstance()
//                            try {
//                                c.time = format.parse(forecast.date)
//                            } catch (e: ParseException) {
//                                e.printStackTrace()
//                            }
//
//                            var dayForWeek = ""
//                            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//                                dayForWeek = "周日"
//                            } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
//                                dayForWeek = "周一"
//                            } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
//                                dayForWeek = "周二"
//                            } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
//                                dayForWeek = "周三"
//                            } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
//                                dayForWeek = "周四"
//                            } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
//                                dayForWeek = "周五"
//                            } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//                                dayForWeek = "周六"
//                            }
//
//                            editor.putString("date" + i, dayForWeek)
//                            editor.putString("forecast_info" + i, forecast.more.info)
//                            editor.putInt("temperature_max" + i, forecast.temperature.max)
//                            editor.putInt("temperature_min" + i, forecast.temperature.min)
//                            editor.commit()
//                        }
//                        runOnUiThread {
//                            val isLatest_pref = getSharedPreferences("isLatest_pref", Context.MODE_PRIVATE)
//                            val Latest_city = isLatest_pref.getString("Latest_city", "init")
//                            val updata_time_pref = getSharedPreferences("weather_pref", Context.MODE_PRIVATE)
//                            val city = updata_time_pref.getString("city", "init")
//
//                            if (weather != null && "ok" == weather!!.status || Latest_city != city) {
//                                changeWeatherView()
//                                lineview.invalidate()
//                                DisplayToast("刷新成功")
//                                val editor = getSharedPreferences("isLatest_pref", Context.MODE_PRIVATE).edit()
//                                editor.putString("Latest_time", weather!!.basic.update.loc)
//                                editor.putString("Latest_city", weather!!.basic.city)
//                                editor.commit()
//                            } else {
//                            }
//                            swipe_refresh.isRefreshing = false
//                        }
//                    } else {
//                        runOnUiThread {
//                            swipe_refresh.isRefreshing = false
//                            DisplayToast("总请求过多，请明天再试")
//                        }
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//            }
//        })
//    }

}
