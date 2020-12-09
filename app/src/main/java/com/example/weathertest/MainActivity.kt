package com.example.weathertest


import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weathertest.data.WeatherResponse
import com.example.weathertest.database.DataBaseHandler
import com.example.weathertest.injection.component.DaggerOpenWeatherApiComponent
import com.example.weathertest.injection.module.OpenWeatherAPIModule
import com.example.weathertest.ui.MainView
import com.example.weathertest.ui.RecyclerAdapter
import com.example.weathertest.ui.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), MainView {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private var weatherList = arrayListOf<WeatherResponse> ()
    private val db = DataBaseHandler(this)
    private var currentForecast = WeatherResponse()
    private val location =  LocationNow(this)
    private val presenter = MainPresenter(this)
    private val PERMISSION_ID = 10;
    private val language = Locale.getDefault().country;

    private fun adapter(){
        linearLayoutManager = LinearLayoutManager(this)
        favoriteRecylceView.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(this, weatherList)
        favoriteRecylceView.adapter = adapter
    }

    private fun getForecast(city: String, unit: String, lang: String ) = presenter.getForecastForCity(city, unit, lang)

    private fun getForecastById(id:Int, unit: String, lang: String) = presenter.getForecastById(id, unit, lang)

    private fun getLocationForecast(lat: Double, lon: Double, unit: String, lang: String) = presenter.getForecastLocation(lat, lon, unit, lang)

    private fun Activity.toast(toastMessage: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, toastMessage, duration).show() }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDI()
        getLocation()
        getAll()
        adapter()
        playAnimation()

        darkmodeswitch.setOnClickListener {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Configuration.UI_MODE_NIGHT_NO ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }
        }
    }

    override fun injectDI() {
        DaggerOpenWeatherApiComponent
            .builder()
            .openWeatherAPIModule(OpenWeatherAPIModule())
            .build()
            .inject(presenter)
    }

    override fun isChecked() : Boolean{
        return switchersystem.isChecked
    }

    override fun playAnimation() {
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        val stb = AnimationUtils.loadAnimation(this, R.anim.stb)
        currentWeather.startAnimation(ttb)

        humidityIcon.startAnimation(stb)
        windIcon.startAnimation(stb)
        maxTempIcon.startAnimation(stb)
    }

    override fun toTop() {
        mainScrollView.fullScroll(ScrollView.FOCUS_UP)
    }

    override fun deleteFavorite(weatherResponse: WeatherResponse) {
            weatherList.remove(weatherResponse)
            db.deleteOneName(weatherResponse.cityName)
            weatherList.clear()
            favoriteBox.isChecked = db.getOneName(currentForecast.cityName)?.isChecked ?: false
            favoriteBox.isEnabled = !favoriteBox.isChecked
            playAnimation()
            toTop()
            getAll()
            getForecast(currentForecast.cityName, "metric", language)
            adapter.notifyDataSetChanged()
    }

    override fun hideSpinner() {
        forecastList.visibility = View.VISIBLE
        loadingSpinner.visibility = View.GONE
    }

    override fun getLocation(){
        if (location.checkPermission()){
            if (location.isLocationEnable()){
                location.getLastLocation().addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        if (isChecked()) {
                            getLocationForecast(location.latitude, location.longitude, "imperial", language)
                        } else {
                            getLocationForecast(location.latitude, location.longitude, "metric", language)
                        }
                    }
                }
            }
        }else{
            requestPermission()
            getLocation()
        }
    }


     override fun getAll() {
        val list = db.readID()
        for (args in list) {
            if (isChecked()) {
                getForecastById(args, "imperial", language)
            } else {
                getForecastById(args, "metric", language)
            }
        }
    }
        override fun updateFavorite(weatherResponse: WeatherResponse) {
            weatherList.add(weatherResponse)
            adapter.notifyItemInserted(weatherList.size)
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun updateForecast(weatherResponse: WeatherResponse) {
            currentForecast = weatherResponse
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            if(isChecked()){
                degreeText.text = "${weatherResponse.forecast.temperature.toDouble().toInt()} °F"
                windText.text = "${weatherResponse.wind.windSpeed} m/s"
                maxTempText.text = "${weatherResponse.forecast.tempMax.toDouble().toInt()} °F"
            }else{
                windText.text = "${weatherResponse.wind.windSpeed} km/h"
                maxTempText.text = "${weatherResponse.forecast.tempMax.toDouble().toInt()} °C"
                degreeText.text = "${weatherResponse.forecast.temperature.toDouble().toInt()} °C"
            }
            switchersystem.setOnClickListener {
                if (isChecked()) {
                    getForecast(weatherResponse.cityName, "imperial", language)
                    switchersystem.text = "Imperial"

                } else {
                    getForecast(weatherResponse.cityName, "metric", language)
                    switchersystem.text = "Metric"
                }
                weatherList.clear()
                adapter.notifyDataSetChanged()
                playAnimation()
                getAll()
            }
            weatherStatus.text = weatherResponse.weather[0].description
            humidityText.text = "${weatherResponse.forecast.humidity} %"
            dateText.text = sdf.format(Date())
            cityName.text = weatherResponse.cityName
            favoriteBox.isChecked = db.getOneName(weatherResponse.cityName)?.isChecked ?: false
            favoriteBox.isEnabled = !favoriteBox.isChecked
            favoriteBox.setOnClickListener {
                if (favoriteBox.isChecked) {
                    db.insertData(weatherResponse)
                    updateFavorite(weatherResponse)
                    favoriteBox.isEnabled = false
                }
            }
            Glide.with(weatherIcon.context)
                    .load("https://openweathermap.org/img/w/${weatherResponse.weather[0].icon}.png")
                    .into(weatherIcon)
        }


        override fun showSpinner() {
            forecastList.visibility = View.GONE
            emptyStateText.visibility = View.GONE
            loadingSpinner.visibility = View.VISIBLE
        }

        override fun showErrorToast(errorType: ErrorTypes) {
            when (errorType) {
                ErrorTypes.CALL_ERROR -> toast(getString(R.string.connection_error_message))
                ErrorTypes.MISSING_API_KEY -> toast(getString(R.string.missing_api_key_message))
                ErrorTypes.NO_RESULT_FOUND -> toast(getString(R.string.city_not_found_toast_message))
            }
            loadingSpinner.visibility = View.GONE
            emptyStateText.visibility = View.VISIBLE
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu, menu)

            val menuItem = menu?.findItem(R.id.search_button)
            val searchMenuItem = menuItem?.actionView

            if (searchMenuItem is SearchView) {
                searchMenuItem.queryHint = getString(R.string.menu_search_hint)
                searchMenuItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {

                        return if (isChecked()) {
                            getForecast(query, "imperial", language)
                            menuItem.collapseActionView()
                            false
                        } else {
                            getForecast(query, "metric", language)
                            menuItem.collapseActionView()
                            false
                        }
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }
            return true
        }
}
