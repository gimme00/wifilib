package com.selpic.wifilib.sample.ui.guide

import android.util.Log
import androidx.compose.Composable
import androidx.compose.State
import androidx.ui.core.Alignment
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.layout.FixedSpacer
import androidx.ui.layout.Stack
import androidx.ui.layout.WidthSpacer
import androidx.ui.material.Button
import androidx.ui.material.ContainedButtonStyle
import androidx.ui.material.LinearProgressIndicator
import androidx.ui.material.OutlinedButtonStyle
import com.selpic.sdk.wifilib.android.model.PacketType
import com.selpic.wifilib.sample.ktx.pass
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