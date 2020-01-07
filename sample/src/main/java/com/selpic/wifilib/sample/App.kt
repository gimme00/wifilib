package com.selpic.wifilib.sample

import android.app.Application
import android.content.Context
import com.selpic.sdk.wifilib.android.impl.SelpicPrinterFactory

class App : Application() {
    companion object {
        lateinit var appContext: Context
        val printer by lazy {
            SelpicPrinterFactory.create(appContext)
        }
    }

    init {
        appContext = this
    }
}