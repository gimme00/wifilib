package com.selpic.wifilib.sample.ktx

import androidx.compose.Composable

typealias ComposableFuncation = @Composable() () -> Unit

typealias ScopedComposableFunction<T> = @Composable() T.() -> Unit