package com.selpic.wifilib.sample

import android.app.Application
import android.content.Context
import com.selpic.sdk.wifilib.android.SelpicPrinter

class App : Application() {
    companion object {
        lateinit var appContext: Context
        val printer by lazy {
            SelpicPrinter.create(appContext)
        }
    }
    init {
        appContext = this
    }
}