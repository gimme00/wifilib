package com.selpic.wifilib.sample.ui.guide

import android.content.Intent
import android.provider.Settings
import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.ContextAmbient
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.material.Button
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.selpic.sdk.wifilib.android.model.DeviceInfo
import com.selpic.wifilib.sample.App
import com.selpic.wifilib.sample.ktx.subscribeOrToast
import com.selpic.wifilib.sample.ktx.withOpacity
import com.selpic.wifilib.sample.ui.widget.TextColorDivider

@Composable
fun GuideScreen() {
    val (deviceInfo, setDeviceInfo) = +state<DeviceInfo?> { null }
    VerticalScroller() {
        Column {
            Step(
                index = 0,
                title = "Connect to Printer",
                summary = "The Printer's Wifi name start with `Selpic_` "
            ) {
                val context = +ambient(ContextAmbient)
                Button(text = "Open Wifi", onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_WIFI_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                })
            }
            TextColorDivider()
            Step(
                index = 1,
                title = "Obtain Device Info",
                summary = "DeviceInfo: $deviceInfo"
            ) {
                Button("Obtain", onClick = {
                    App.printer.deviceInfo.subscribeOrToast {
                        setDeviceInfo(it)
                    }

                })
            }
            TextColorDivider()
            Step(index = 2) {}
            TextColorDivider()
            Step(index = 3) {}
            TextColorDivider()
            Step(index = 3) {}
            TextColorDivider()
            Step(index = 3) {}
            TextColorDivider()
            Step(index = 3) {}
            TextColorDivider()
            Step(index = 3) {}
            TextColorDivider()
            Step(index = 3) {}
            TextColorDivider()
            Step(index = 3) {}
            TextColorDivider()
            Step(index = 3) {}
        }
    }
}

@Preview
@Composable
fun GuidePagePreview() {
    GuideScreen()
}