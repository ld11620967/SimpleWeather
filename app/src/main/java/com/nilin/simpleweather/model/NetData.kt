package com.nilin.simpleweather.model

data class NetData(
    val HeWeather6: List<HeWeather6>
)

data class HeWeather6(
        val basic: Basic,
        val now: Now,
        val daily_forecast: List<DailyForecast>,
        val status: String,
        val update: Update
)

data class Basic(
    val admin_area: String,
    val cid: String,
    val cnty: String,
    val lat: String,
    val location: String,
    val lon: String,
    val parent_city: String,
    val tz: String
)

data class Now(
    val cloud: String,
    val cond_code: String,
    val cond_txt: String,
    val fl: String,
    val hum: String,
    val pcpn: String,
    val pres: String,
    val tmp: String,
    val vis: String,
    val wind_deg: String,
    val wind_dir: String,
    val wind_sc: String,
    val wind_spd: String
)

data class DailyForecast(
    val cond_code_d: String,
    val cond_code_n: String,
    val cond_txt_d: String,
    val cond_txt_n: String,
    val date: String,
    val hum: String,
    val mr: String,
    val ms: String,
    val pcpn: String,
    val pop: String,
    val pres: String,
    val sr: String,
    val ss: String,
    val tmp_max: String,
    val tmp_min: String,
    val uv_index: String,
    val vis: String,
    val wind_deg: String,
    val wind_dir: String,
    val wind_sc: String,
    val wind_spd: String
)

data class Update(
    val loc: String,
    val utc: String
)