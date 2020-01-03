package com.selpic.wifilib.sample.ui

import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Text
import androidx.ui.input.KeyboardType
import androidx.ui.layout.Column
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.surface.Surface
import androidx.ui.tooling.preview.Preview
import com.selpic.wifilib.sample.ktx.isNightMode
import com.selpic.wifilib.sample.ui.guide.GuideScreen

@Composable
fun SampleApp() {
    val (isDark, setDark) = +state { (+ambient(ContextAmbient)).isNightMode() }

    MaterialTheme(colors = if (isDark) darkThemeColors else lightThemeColors) {
        Surface {
            Column {
                TopAppBar(title = { Text(AppState.currentScreen.title) })
                AppState.currentScreen.create()
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    SampleApp()
}