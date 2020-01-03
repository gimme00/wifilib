package com.selpic.wifilib.sample.ui

import androidx.compose.Composable
import androidx.compose.Model
import com.selpic.wifilib.sample.ui.guide.GuideScreen

sealed class Screen(val title: String) {
    @Composable
    abstract fun create()

    object Guide : Screen("Guide") {
        override fun create() {
            GuideScreen()
        }
    }

    object Empty : Screen("Empty") {
        override fun create() {

        }
    }
}

@Model
object AppState {
    var currentScreen: Screen = Screen.Guide
}