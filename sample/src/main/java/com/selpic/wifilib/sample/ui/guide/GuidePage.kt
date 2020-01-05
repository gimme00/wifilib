package com.selpic.wifilib.sample.ui.guide

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.provider.Settings
import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.tooling.preview.Preview
import com.selpic.sdk.wifilib.android.model.DeviceInfo
import com.selpic.sdk.wifilib.android.model.PrintParam
import com.selpic.wifilib.sample.App
import com.selpic.wifilib.sample.ktx.*
import com.selpic.wifilib.sample.ui.widget.TextColorDivider
import com.selpic.wifilib.sample.util.AssertFile
import io.reactivex.disposables.Disposable

@Composable
fun GuideScreen() {
    val deviceInfoState = +state<DeviceInfo?> { null }
    VerticalScroller() {
        Column {
            if (deviceInfoState.value == null) {
                StepGroup(deviceInfoState)
            } else {
                FeatureGroup(deviceInfoState)
            }
        }
    }
}

@Composable
fun StepGroup(deviceInfoState: State<DeviceInfo?>) {
    val context = +ambient(ContextAmbient)
    val (deviceInfo, onDeviceInfoChange) = deviceInfoState
    Column {
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
            val loadingState = +state { false }
            LoadingButton(isLoading = loadingState.value, text = "Obtain", onClick = {
                App.printer.deviceInfo
                    .attachLoadingState(loadingState)
                    .subscribeOrToast(onDeviceInfoChange)
            })
        }
    }
}

@Composable
fun FeatureGroup(deviceInfoState: State<DeviceInfo?>) {
    val context = +ambient(ContextAmbient)
    val (deviceInfo, onDeviceInfoChange) = deviceInfoState
    if (deviceInfo == null) {
        return
    }
    Column {
        TextColorDivider()
        Item(
            title = "Feature 1: Receive device info in real time",
            summary = "DeviceInfo: $deviceInfo"
        ) {
            val (disposable, setDisposable) = +state<Disposable?> { null }
            Button(if (disposable == null) "Receive" else "Close", onClick = {
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
        Item(title = "Feature 3: Set print param") {
            val (plusWidth, onPlusWidthChange) = +state<Int?> { 12 }
            val (grayScale, onGrayScaleChange) = +state<Int?> { 1 }
            val (voltage, onVoltageChange) = +state<Int?> { 0 }
            IntTextInput(
                modifier = Spacing(top = 8.dp),
                value = plusWidth,
                onValueChange = onPlusWidthChange,
                name = "PlusWidth",
                defaultValue = 12,
                hint = ""
            )
            IntTextInput(
                modifier = Spacing(top = 8.dp),
                value = grayScale,
                onValueChange = onGrayScaleChange,
                name = "ScaleState",
                defaultValue = 1,
                hint = ""
            )
            IntTextInput(
                modifier = Spacing(top = 8.dp, bottom = 8.dp),
                value = voltage,
                onValueChange = onVoltageChange,
                name = "Voltage",
                defaultValue = 0,
                hint = ""
            )

            val isSendingState = +state { false }
            LoadingButton(isLoading = isSendingState.value, text = "Set", onClick = {
                App.printer.setPrintParam(
                    PrintParam(
                        plusWidth ?: 12,
                        grayScale ?: 1,
                        voltage ?: 0
                    )
                )
                    .attachLoadingState(isSendingState)
                    .subscribeOrToast(::pass)
            })
        }
        TextColorDivider()
        Item(title = "Feature 4: Print") {
            val progressState = +state<Float?> { null }
         // val view: View = +ambient(AndroidComposeViewAmbient)
            ProgressButton(progress = progressState.value, text = "Print", onClick = {
                val height = deviceInfo.pointPreColumn
                val bitmap = Bitmap.createBitmap(height, height, Bitmap.Config.RGB_565)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.textSize = height / 2f
                with(Canvas(bitmap)) {
                    drawText("Selpic WIFI SDK", height / 2f, 0f, paint)
                }
                App.printer.sendPrintData(bitmap)
                    .attachProgressState(progressState)
                    .subscribeOrToast(::pass)
            })
        }
        TextColorDivider()
    }
}

@Preview
@Composable
fun GuidePagePreview() {
    GuideScreen()
}
