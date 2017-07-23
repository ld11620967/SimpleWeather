package com.nilin.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {
        public int max;
        public int min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }

}
