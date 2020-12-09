package com.example.weathertest.ui

import com.example.weathertest.ErrorTypes
import com.example.weathertest.data.WeatherResponse

interface MainView{
    fun showSpinner()
    fun hideSpinner()
    fun getLocation()
    fun injectDI()
    fun getAll()
    fun isChecked() : Boolean
    fun playAnimation()
    fun toTop()
    fun deleteFavorite(weatherResponse: WeatherResponse)
    fun updateFavorite(weatherResponse: WeatherResponse)
    fun updateForecast(weatherResponse: WeatherResponse)
    fun showErrorToast(errorType: ErrorTypes)

}