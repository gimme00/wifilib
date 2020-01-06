package com.selpic.wifilib.sample.ui.guide

import androidx.compose.Composable
import androidx.compose.State
import androidx.ui.core.Text
import androidx.ui.material.Button
import androidx.ui.material.OutlinedButtonStyle
import io.reactivex.Single

fun <T> Single<T>.attachLoadingState(state: State<Boolean>) = this
    .doOnSubscribe { state.value = true }
    .doOnTerminate { state.value = false }

@Composable
fun LoadingButton(isLoading: Boolean, text: String, onClick: () -> Unit) {
    Button(style = OutlinedButtonStyle(), onClick = if (!isLoading) onClick else null) {
        Text(text = if (!isLoading) text else "Loading...")
    }
}