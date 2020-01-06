package com.selpic.wifilib.sample.ui

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Text
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.layout.Column
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.surface.Surface
import androidx.ui.tooling.preview.Preview

@Composable
fun SampleApp() {
    MaterialTheme(colors = if (+isSystemInDarkTheme()) darkThemeColors else lightThemeColors) {
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