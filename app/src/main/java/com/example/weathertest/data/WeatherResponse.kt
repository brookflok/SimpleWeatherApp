package com.example.weathertest.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse (@SerializedName("id")  var cityID: Long= 1,
                            @SerializedName("name") var cityName : String="",
                            @SerializedName("dt") var  date: Long= 0,
                            @SerializedName("main") var forecast : ForecastDetail = ForecastDetail(),
                            @SerializedName("weather") var weather: List<WeatherDescription> = emptyList(),
                            @SerializedName("wind") var wind : windData  = windData()
) {
    var isChecked: Boolean = false
    var weatherid: Int = 0

}
