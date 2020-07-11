package com.example.demo.app

import com.example.demo.view.MainView
import com.example.demo.view.Payment
import tornadofx.App

class MyApp: App(MainView::class, Styles::class){
    override val primaryView = MainView::class


}