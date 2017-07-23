package com.nilin.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    public Basic basic;
    public Now now;
    public String status;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
