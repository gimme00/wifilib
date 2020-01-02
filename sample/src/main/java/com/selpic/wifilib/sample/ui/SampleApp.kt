package com.selpic.wifilib.sample.ui

import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.tooling.preview.Preview
import com.selpic.wifilib.sample.App
import com.selpic.wifilib.sample.ktx.isNightMode

@Composable
fun SampleApp() {
    val (isDark, setDark) = +state { (+ambient(ContextAmbient)).isNightMode() }
    MaterialTheme(colors = if (isDark) darkThemeColors else lightThemeColors) {
        Surface {
            Text("Hello")
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    SampleApp()
}