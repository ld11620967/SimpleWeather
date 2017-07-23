package com.nilin.simpleweather.model

/**
 * Created by nilin on 2017/7/23.
 */
data class Result(val temperature:String,val city:String,val weather:String,val updateTime:String,val future:List<Future>){

}