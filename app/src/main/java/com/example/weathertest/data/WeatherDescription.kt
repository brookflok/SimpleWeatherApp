package com.example.weathertest.data

import com.google.gson.annotations.SerializedName

data class WeatherDescription (@SerializedName("main") var cityName : String,
                                @SerializedName("description") var description : String,
                                @SerializedName("icon") var icon : String)
