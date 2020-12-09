package com.example.weathertest.ui.presenter

import com.example.weathertest.api.OpenWeatherAPI
import com.example.weathertest.ErrorTypes
import com.example.weathertest.data.WeatherResponse
import com.example.weathertest.ui.MainView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class MainPresenter(val view : MainView) {
    @Inject
    lateinit var api : OpenWeatherAPI


    fun getForecastLocation (lat:Double, lon:Double, units: String, lang: String){
        view.showSpinner()
        api.locationForecast(lat,lon,units, lang).enqueue(object : Callback<WeatherResponse>{
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                view.showErrorToast(ErrorTypes.CALL_ERROR)
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                response.body()?.let {

                    createListforView(it)
                    view.hideSpinner()
                } ?: view.showErrorToast(ErrorTypes.NO_RESULT_FOUND)
            }

        })
    }

    fun getForecastForCity (cityName : String, units : String, lang: String) {

        view.showSpinner()
        api.dailyForecast(cityName, units, lang).enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                response.body()?.let {

                    createListforView(it)
                    view.hideSpinner()
                    view.playAnimation()
                } ?: view.showErrorToast(ErrorTypes.NO_RESULT_FOUND)
            }

            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable) {
                view.showErrorToast(ErrorTypes.CALL_ERROR)
                t.printStackTrace()
            }
        })
    }

    fun getForecastById (id : Int, units : String, lang: String) {

        api.getForecastById(id , units, lang).enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                response.body()?.let {
                    updateFavorite(it)
                } ?: view.showErrorToast(ErrorTypes.NO_RESULT_FOUND)
            }

            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    private fun updateFavorite(weatherResponse: WeatherResponse){
        view.updateFavorite(weatherResponse)
    }


    private fun createListforView(weatherResponse: WeatherResponse){

        view.updateForecast(weatherResponse)

    }

}

