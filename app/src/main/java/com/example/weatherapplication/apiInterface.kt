package com.example.weatherapp

import com.example.weatherapplication.weatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface apiInterface {
    @GET("forecast")
    fun getWeatherData(
        @Query("q") city: String?,
        @Query("appid") appid: String,
        @Query("units") units: String
    ) : Call<weatherData>

}