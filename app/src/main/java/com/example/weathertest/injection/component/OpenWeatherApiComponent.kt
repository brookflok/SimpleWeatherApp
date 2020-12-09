package com.example.weathertest.injection.component

import com.example.weathertest.injection.module.OpenWeatherAPIModule
import com.example.weathertest.ui.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [OpenWeatherAPIModule::class])
interface OpenWeatherApiComponent{
    fun inject(presenter: MainPresenter)
}