package com.example.weathertest.data
import com.google.gson.annotations.SerializedName

data class ForecastDetail(@SerializedName("temp") var temperature : String = "",
                          @SerializedName("temp_min") var tempMin : String = "",
                          @SerializedName("temp_max") var tempMax : String = "",
                          @SerializedName("pressure") var pressure : Double= 0.0,
                          @SerializedName("humidity") var humidity :Double= 0.0,
                          @SerializedName("feels_like") var feelsLike : Double= 0.0 )
