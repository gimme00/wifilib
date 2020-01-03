package com.selpic.wifilib.sample.ui.guide

import android.content.Intent
import android.provider.Settings
import androidx.compose.*
import androidx.ui.core.ContextAmbient
import androidx.ui.core.dp
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.tooling.preview.Preview
import com.selpic.sdk.wifilib.android.model.DeviceInfo
import com.selpic.sdk.wifilib.android.model.PacketType
import com.selpic.wifilib.sample.App
import com.selpic.wifilib.sample.ktx.*
import com.selpic.wifilib.sample.ui.widget.TextColorDivider
import com.selpic.wifilib.sample.util.AssertFile
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

@Composable
fun GuideScreen() {
    val context = +ambient(ContextAmbient)
    val (deviceInfo, onDeviceInfoChange) = +state<DeviceInfo?> { null }
    VerticalScroller() {
        Column {
            StepGroup {
                Item(
                    title = "Step 1: Connect to Printer",
                    summary = "The Printer's Wifi name start with `Selpic_` "
                ) {
                    Button(text = "Open Wifi", onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_WIFI_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    })
                }
                TextColorDivider()
                Item(
                    title = "Step 2: Obtain Device Info",
                    summary = "DeviceInfo: $deviceInfo"
                ) {
                    Button("Obtain", onClick = {
                        App.printer.deviceInfo.subscribeOrToast(onDeviceInfoChange)
                    })
                }
            }
            if (deviceInfo != null) {
                FeatureGroup {
                    TextColorDivider()
                    Item(title = "Feature 1: Receive device info in real time") {
                        val (disposable, setDisposable) = +state<Disposable?> { null }
                        Button(if (disposable == null) "RECV" else "Close", onClick = {
                            if (disposable == null) {
                                setDisposable(
                                    App.printer.receiveDeviceInfo().subscribeOrToast(
                                        onDeviceInfoChange
                                    )
                                )
                            } else {
                                if (disposable.isDisposed) {
                                    disposable.dispose()
                                }
                                setDisposable(null)
                            }
                        })
                    }
                    TextColorDivider()
                    Item(title = "Feature 2: OTA") {
                        val progressState = +state<Float?> { null }
                        val code = 18
                        ProgressButton(progress = progressState.value, text = "OTA", onClick = {
                            App.printer.sendOta(
                                AssertFile(
                                    "ota/${deviceInfo.typeName}_2.7_$code.bin",
                                    context.assets
                                ),
                                code
                            )
                                .attachProgressState(progressState)
                                .subscribeOrToast(::pass)
                        })
                        HeightSpacer(height = 8.dp)
                        val printDataOtaProgressState = +state<Float?> { null }
                        ProgressButton(
                            progress = printDataOtaProgressState.value,
                            text = "Print Data OTA",
                            onClick = {
                                App.printer.sendPrintDataOta(
                                    AssertFile(
                                        "ota/${deviceInfo.typeName}_2.7_$code.print.bin",
                                        context.assets
                                    )
                                )
                                    .attachProgressState(printDataOtaProgressState)
                                    .subscribeOrToast(::pass)
                            }
                        )
                    }
                    TextColorDivider()
                }
            }
        }
    }
}

@Composable
fun StepGroup(children: ScopedComposableFunction<ColumnScope>) {
    Column(children = children)
}
@Composable
fun FeatureGroup(children: ScopedComposableFunction<ColumnScope>) {
    Column(children = children)
}

@Preview
@Composable
fun GuidePagePreview() {
    GuideScreen()
}