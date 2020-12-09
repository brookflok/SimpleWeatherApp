package com.example.weathertest.ui.presenter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathertest.data.WeatherResponse
import com.example.weathertest.database.DataBaseHandler
import com.example.weathertest.ui.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.forecast_list_item.view.*

class ForecastHolder(v:View) : RecyclerView.ViewHolder(v), View.OnClickListener{
    private var view: View = v

    @SuppressLint("SetTextI18n")
    fun BindForecast(mainView: MainView, weatherResponse: ArrayList<WeatherResponse>) {

        Glide.with(view.itemImage.context)
                .load("https://openweathermap.org/img/w/${weatherResponse[adapterPosition].weather[0].icon}.png")
                .into(view.itemImage)

        view.deleteicon.setOnClickListener(View.OnClickListener {
            mainView.deleteFavorite(weatherResponse[adapterPosition])
        })
        view.itemDate.text = weatherResponse[adapterPosition].cityName
        if (mainView.isChecked()){
            view.itemDescription.text =
                weatherResponse[adapterPosition].forecast.temperature.toDouble().toInt().toString() + " °F"
        }else {

            view.itemDescription.text =
                weatherResponse[adapterPosition].forecast.temperature.toDouble().toInt().toString() + " °C"
        }


    }

    init {
        v.setOnClickListener(this)
    }




    companion object {

    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}
