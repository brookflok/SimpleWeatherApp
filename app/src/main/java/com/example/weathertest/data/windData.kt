package com.example.weathertest.data

import com.google.gson.annotations.SerializedName

data class windData (@SerializedName("speed") var windSpeed : Double = 0.0,
                     @SerializedName("deg") var windDeg: Int = 0)