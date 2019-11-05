package com.nilin.simpleweather.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast

import com.nilin.simpleweather.Api.Api
import com.nilin.simpleweather.model.NetData
import com.nilin.simpleweather.R
import com.nilin.simpleweather.utils.ActivityCollector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {
    private var mytoast: Toast? = null
    private var mExitTime: Long = 0
    var isExit = false
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
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
        val intent = intent
        ActivityCollector.addActivity(this)
        swipe_refresh.isRefreshing = true
        val a = intent.getIntExtra("isRefreshing", 0)

        if (a == 0) {
            swipe_refresh.isRefreshing = true
            val pref = getSharedPreferences("settings_pref", Context.MODE_PRIVATE)
            val city = pref.getString("city", "")
            getWeatherData(city!!)
        } else if (a == 1) {
            swipe_refresh.isRefreshing = true
            isUpdataWeather()
        }
        swipe_refresh.setOnRefreshListener { isUpdataWeather() }
        Thread(lineview::postInvalidate).start()
    }

    fun isUpdataWeather() {
        val pref = getSharedPreferences("settings_pref", Context.MODE_PRIVATE)
        val city = pref.getString("city", "")
        getWeatherData(city!!)
        swipe_refresh.isRefreshing = false
    }

    @SuppressLint("CheckResult")
    protected fun getWeatherData(city: String) {
        val api = Api.Factory.create()
        api.getData(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ weather ->
                    parseResult(weather)
                }, { _ ->

                })
    }

    fun parseResult(netData: NetData) {
        if (netData.HeWeather6[0].status != "ok") {
            DisplayToast("获取最新天气失败")
        } else {
            val editor = getSharedPreferences("weather_pref", Context.MODE_PRIVATE).edit()
            editor.putString("city", netData.HeWeather6[0].basic.location)
            editor.putString("updata_time", netData.HeWeather6[0].update.loc.substring(5,16))
            editor.putString("temperature", netData.HeWeather6[0].daily_forecast[0].tmp_max)
            editor.putString("now_info", netData.HeWeather6[0].daily_forecast[0].cond_txt_d)

            for (daily_forecast in netData.HeWeather6[0].daily_forecast) {
                i = i + 1
                try {
                    val editor1 = getSharedPreferences("weather_pref", Context.MODE_PRIVATE).edit()
                    editor1.putString("date$i", daily_forecast.date.substring(5,10))
                    editor1.putString("forecast_info$i", daily_forecast.cond_txt_d)
                    editor1.putInt("temperature_max$i", daily_forecast.tmp_max.toInt())
                    editor1.putInt("temperature_min$i", daily_forecast.tmp_min.toInt())
                    editor1.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            editor.apply()
            changeWeatherView()
        }
        swipe_refresh.isRefreshing = false
    }

    fun changeWeatherView() {
        val pref = getSharedPreferences("weather_pref", Context.MODE_PRIVATE)
        val city1 = pref.getString("city", "")
        val updata_time1 = pref.getString("updata_time", "")
        val temperature1 = pref.getString("temperature", "")
        val now_info = pref.getString("now_info", "")

        if (now_info == "晴") {
            weather_now.setBackgroundResource(R.drawable.weather_sunny_bg)
        } else if (now_info!!.contains("云")) {
            weather_now.setBackgroundResource(R.drawable.weather_cloudy_bg)
        } else if (now_info == "阴") {
            weather_now.setBackgroundResource(R.drawable.weather_overcast_bg)
        } else if (now_info.contains("雨")) {
            weather_now.setBackgroundResource(R.drawable.weather_rain_bg)
        } else if (now_info.contains("雪")) {
            weather_now.setBackgroundResource(R.drawable.weather_snow_bg)
        } else {
            weather_now.setBackgroundResource(R.drawable.weather_foggy_bg)
        }

        city.text = city1
        updata_time.text = updata_time1
        temperature.text = temperature1
        info.text = now_info

        val forecast_info1 = pref.getString("forecast_info1", "")
        val forecast_info2 = pref.getString("forecast_info2", "")
        val forecast_info3 = pref.getString("forecast_info3", "")
        val forecast_info4 = pref.getString("forecast_info4", "")
        val forecast_info5 = pref.getString("forecast_info5", "")
        val forecast_info6 = pref.getString("forecast_info6", "")
        val forecast_info7 = pref.getString("forecast_info7", "")

        weather1.text = pref.getString("date1", "")
        weather2.text = pref.getString("date2", "")
        weather3.text = pref.getString("date3", "")
        weather4.text = pref.getString("date4", "")
        weather5.text = pref.getString("date5", "")
        weather6.text = pref.getString("date6", "")
        weather7.text = pref.getString("date7", "")

        weather_info1.text = forecast_info1
        weather_info2.text = forecast_info2
        weather_info3.text = forecast_info3
        weather_info4.text = forecast_info4
        weather_info5.text = forecast_info5
        weather_info6.text = forecast_info6
        weather_info7.text = forecast_info7

        if (forecast_info1!!.contains("晴")||forecast_info1.contains("风")) {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info1.contains("云")) {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info1 == "阴") {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info1 == "小雨") {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info1 == "中雨") {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info1.contains("雷雨")) {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info1.contains("雨")) {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info1.contains("雪")) {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon1.setBackgroundResource(R.drawable.weather_fog)
        }

        if (forecast_info2!!.contains("晴")||forecast_info2.contains("风")) {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info2.contains("云")) {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info2 == "阴") {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info2 == "小雨") {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info2 == "中雨") {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info2.contains("雷雨")) {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info2.contains("雨")) {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info2.contains("雪")) {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon2.setBackgroundResource(R.drawable.weather_fog)
        }

        if (forecast_info3!!.contains("晴")||forecast_info3.contains("风")) {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info3.contains("云")) {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info3 == "阴") {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info3 == "小雨") {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info3 == "中雨") {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info3.contains("雷雨")) {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info3.contains("雨")) {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info3.contains("雪")) {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon3.setBackgroundResource(R.drawable.weather_fog)
        }

        if (forecast_info4!!.contains("晴")||forecast_info4.contains("风")) {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info4.contains("云")) {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info4 == "阴") {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info4 == "小雨") {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info4 == "中雨") {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info4.contains("雷雨")) {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info4.contains("雨")) {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info4.contains("雪")) {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon4.setBackgroundResource(R.drawable.weather_fog)
        }

        if (forecast_info5!!.contains("晴")||forecast_info5.contains("风")) {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info5.contains("云")) {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info5 == "阴") {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info5 == "小雨") {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info5 == "中雨") {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info5.contains("雷雨")) {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info5.contains("雨")) {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info5.contains("雪")) {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon5.setBackgroundResource(R.drawable.weather_fog)
        }

        if (forecast_info6!!.contains("晴")||forecast_info6.contains("风")) {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info6.contains("云")) {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info6 == "阴") {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info6 == "小雨") {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info6 == "中雨") {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info6.contains("雷雨")) {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info6.contains("雨")) {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info6.contains("雪")) {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon6.setBackgroundResource(R.drawable.weather_fog)
        }

        if (forecast_info7!!.contains("晴")||forecast_info7.contains("风")) {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_sunny)
        } else if (forecast_info7.contains("云")) {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_cloudy)
        } else if (forecast_info7 == "阴") {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_overcast_sky)
        } else if (forecast_info7 == "小雨") {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_light_rain)
        } else if (forecast_info7 == "中雨") {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_moderate_rain)
        } else if (forecast_info7.contains("雷雨")) {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_thundershower)
        } else if (forecast_info7.contains("雨")) {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_heavy_rain)
        } else if (forecast_info7.contains("雪")) {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_snow)
        } else {
            weather_info_icon7.setBackgroundResource(R.drawable.weather_fog)
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

    @SuppressLint("ShowToast")
    fun DisplayToast(str: String) {
        if (mytoast == null) {
            mytoast = Toast.makeText(this@MainActivity, str, Toast.LENGTH_SHORT)
        } else {
            mytoast!!.setText(str)
        }
        mytoast!!.show()
    }

}