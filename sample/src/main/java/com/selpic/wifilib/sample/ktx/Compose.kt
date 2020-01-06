package com.selpic.wifilib.sample.ktx

import androidx.compose.*
import androidx.ui.core.AndroidComposeViewAmbient
import androidx.ui.core.currentTextStyle
import androidx.ui.graphics.Color
import androidx.ui.material.MaterialTheme

typealias ComposableFunction = @Composable() () -> Unit

typealias ScopedComposableFunction<T> = @Composable() T.() -> Unit

fun currentTextColor() =
    effectOf<Color> {
        (+currentTextStyle()).color ?: (+MaterialTheme.colors()).onSurface
    }

fun currentComposeView() = effectOf<View> {
    +ambient(AndroidComposeViewAmbient as Ambient<View>)
}

class Ref<T>(var value: T) {
    override fun toString(): String {
        return "Ref(value=$value)"
    }
}