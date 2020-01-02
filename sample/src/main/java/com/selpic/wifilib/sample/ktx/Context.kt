package com.selpic.wifilib.sample.ktx

import android.content.Context
import android.content.res.Configuration

//private object Context

fun Context.isNightMode() = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES