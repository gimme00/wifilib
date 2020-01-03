package com.selpic.wifilib.sample.ktx

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast

//private object Context

fun Context.isNightMode() = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

inline fun Context.toast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

inline fun Context.toast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()