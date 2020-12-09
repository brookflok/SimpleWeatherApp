package com.example.weathertest.api

import com.example.weathertest.BuildConfig

import com.example.weathertest.data.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenWeatherAPI {

    @GET("/data/2.5/weather")
    fun dailyForecast(@Query("q") cityName : String,
                      @Query("units") units : String,
                      @Query("lang") lang: String) : Call<WeatherResponse>

    @GET("/data/2.5/weather")
    fun locationForecast(@Query("lat") latitude: Double,
                         @Query("lon") longitude: Double,
                         @Query("units") units:String,
                         @Query("lang") lang: String) : Call<WeatherResponse>

    @GET("/data/2.5/weather")
    fun getForecastById(@Query("id") cityId: Int,
                        @Query("units") units: String,
                        @Query("lang") lang: String) : Call<WeatherResponse>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
    }
}
