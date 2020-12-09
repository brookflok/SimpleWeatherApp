package com.example.weathertest.ui

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.weathertest.R
import com.example.weathertest.data.WeatherResponse
import com.example.weathertest.database.DataBaseHandler
import com.example.weathertest.inflate
import com.example.weathertest.injection.component.DaggerOpenWeatherApiComponent
import com.example.weathertest.injection.module.OpenWeatherAPIModule
import com.example.weathertest.ui.presenter.ForecastHolder
import com.example.weathertest.ui.presenter.MainPresenter
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAdapter(v: MainView, private val forecast: ArrayList<WeatherResponse>): RecyclerView.Adapter<ForecastHolder>() {

    var view = v
    val lang = Locale.getDefault().displayLanguage

    private val presenter = MainPresenter(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        val inflatedView = parent.inflate(R.layout.forecast_list_item, false)
        return ForecastHolder(inflatedView)
    }

    private fun getForecast(city: String, unit: String, lang: String ) = presenter.getForecastForCity(city, unit, lang)

    override fun getItemCount() = forecast.size
    override fun onBindViewHolder(holder:ForecastHolder, position: Int) {
        holder.BindForecast(view, forecast)
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation_fall_down)
        holder.itemView.setOnClickListener(View.OnClickListener {

            DaggerOpenWeatherApiComponent
                    .builder()
                    .openWeatherAPIModule(OpenWeatherAPIModule())
                    .build()
                    .inject(presenter)
            if(view.isChecked())
                getForecast(forecast[position].cityName, "imperial", lang)
            else
                getForecast(forecast[position].cityName, "metric", lang)

            view.toTop()
            view.playAnimation()

        })
    }
}

