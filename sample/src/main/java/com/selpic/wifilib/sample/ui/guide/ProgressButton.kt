package com.selpic.wifilib.sample.ui.guide

import androidx.compose.Composable
import androidx.compose.State
import androidx.ui.core.Text
import androidx.ui.material.Button
import androidx.ui.material.LinearProgressIndicator
import androidx.ui.material.OutlinedButtonStyle
import com.selpic.sdk.wifilib.android.model.PacketType
import io.reactivex.Observable

fun Observable<PacketType>.attachProgressState(state: State<Float?>) =
    this.doOnSubscribe { state.value = Float.NaN }
        .doOnTerminate { state.value = null }
        .doOnNext {
            state.value = when (it) {
                is PacketType.Start -> 0f
                is PacketType.Sub -> (it.index + 1) / it.total.toFloat()
                is PacketType.End -> 1f
                else -> return@doOnNext
            }
        }

@Composable
fun ProgressButton(progress: Float?, text: String, onClick: () -> Unit) {
    // Log.d("wifilib", "ProgressButton(progress=$progress)")
    Button(
        onClick = if (progress == null) onClick else null,
        style = OutlinedButtonStyle()
    ) {
        when {
            progress == null -> Text(text = text)
            progress.isNaN() -> LinearProgressIndicator()
            else -> LinearProgressIndicator(progress = progress)
        }
    }
}