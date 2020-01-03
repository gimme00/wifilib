package com.selpic.wifilib.sample.ktx

import androidx.ui.graphics.Color

fun Color.withOpacity(opacity: Float) = this.copy(alpha = this.alpha * opacity)