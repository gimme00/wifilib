package com.selpic.wifilib.sample.ui.guide

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.provider.Settings
import android.util.Log
import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.border.Border
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.input.ImeAction
import androidx.ui.layout.Column
import androidx.ui.layout.HeightSpacer
import androidx.ui.layout.Spacing
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.res.imageResource
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import com.selpic.sdk.wifilib.android.model.DeviceInfo
import com.selpic.sdk.wifilib.android.model.PrintParam
import com.selpic.sdk.wifilib.android.util.Mocks
import com.selpic.wifilib.sample.App
import com.selpic.wifilib.sample.R
import com.selpic.wifilib.sample.ktx.currentComposeView
import com.selpic.wifilib.sample.ktx.currentTextColor
import com.selpic.wifilib.sample.ktx.subscribeOrToast
import com.selpic.wifilib.sample.ktx.withOpacity
import com.selpic.wifilib.sample.ui.dividerOpacity
import com.selpic.wifilib.sample.ui.widget.TextColorDivider
import com.selpic.wifilib.sample.util.AssertFile
import io.reactivex.disposables.Disposable
import kotlin.math.roundToInt

private val TAG = "GuideScreen"
@Composable
fun GuideScreen() {
    val deviceInfoState = +state<DeviceInfo?> { null }
    VerticalScroller() {
        Column {
            WithDensity {
                if (deviceInfoState.value == null) {
                    StepGroup(deviceInfoState)
                } else {
                    FeatureGroup(deviceInfoState)
                }
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
fun DensityScope.FeatureGroup(deviceInfoState: State<DeviceInfo?>) {
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
                    .subscribeOrToast { }
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
                        .subscribeOrToast { }
                }
            )
        }
        TextColorDivider()
        Item(title = "Feature 3: Set print param") {
            val (plusWidth, onPlusWidthChange) = +state<Int?> { PrintParam.DEFAULT.prtPlusWidth }
            val (grayScale, onGrayScaleChange) = +state<Int?> { PrintParam.DEFAULT.prtGrayScale }
            val (voltage, onVoltageChange) = +state<Int?> { PrintParam.DEFAULT.prtVoltage }
            IntTextInput(
                modifier = Spacing(top = 8.dp),
                value = plusWidth,
                onValueChange = onPlusWidthChange,
                name = "PlusWidth",
                defaultValue = PrintParam.DEFAULT.prtPlusWidth,
                hint = ""
            )
            IntTextInput(
                modifier = Spacing(top = 8.dp),
                value = grayScale,
                onValueChange = onGrayScaleChange,
                name = "GrayScale",
                defaultValue = PrintParam.DEFAULT.prtGrayScale,
                hint = ""
            )
            IntTextInput(
                modifier = Spacing(top = 8.dp, bottom = 8.dp),
                value = voltage,
                onValueChange = onVoltageChange,
                name = "Voltage",
                defaultValue = PrintParam.DEFAULT.prtVoltage,
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
                    .subscribeOrToast { }
            })
        }
        TextColorDivider()
        val inputModel = +memo {
            PrintPreviewItemModel(
                y = (+currentTextStyle()).fontSize.toDp(),
                isCanDrag = true
            )
        }
        val imageModel = +memo { PrintPreviewItemModel(x = 200.dp) }

        Item(title = "Feature 4: Print") {
            Text(
                "Preview",
                modifier = Spacing(left = 8.dp),
                style = (+MaterialTheme.typography()).body2
            )
            var previewCoordinates: LayoutCoordinates? = null
            PrintPreview(
                modifier = Spacing(8.dp),
                shape = RoundedCornerShape(10),
                border = Border(
                    (+currentTextColor()).withOpacity(dividerOpacity),
                    1.dp
                ),
                pointPreColumn = deviceInfo.pointPreColumn,
                onDragStart = { inputModel.isCanDrag = true }
            ) {
                OnPositioned {
                    previewCoordinates = it
                }
                item {
                    Text(+stringResource(R.string.app_name))
                }
                item(inputModel) {
                    val (text, onTextChange) = +state { "TextField" }

                    if (inputModel.isCanDrag) {
                        Clickable(onClick = { inputModel.isCanDrag = false }) {
                            Text(text)
                        }
                    } else {
                        TextField(
                            text,
                            onValueChange = onTextChange,
                            imeAction = ImeAction.Done,
                            onFocus = { inputModel.isCanDrag = false },
                            onBlur = { inputModel.isCanDrag = true }
                        )
                    }
                }
                item(imageModel) {
                    SimpleImage(image = +imageResource(R.drawable.ic_launcher_round))
                }
            }

            val progressState = +state<Float?> { null }
            val view = +currentComposeView()
            ProgressButton(progress = progressState.value, text = "Print", onClick = {
                val coordinates = previewCoordinates ?: return@ProgressButton
                val bitmap = Bitmap.createBitmap(
                    coordinates.size.width.value.roundToInt(),
                    coordinates.size.height.value.roundToInt(),
                    Bitmap.Config.RGB_565
                )

                with(Canvas(bitmap)) {
                    val origin = coordinates.localToRoot(PxPosition.Origin)
                    Log.d(TAG, "draw: $origin")
                    translate(-origin.x.value, -origin.y.value)
                    view.draw(this)
                }
                App.printer.sendPrintData(bitmap)
                    .attachProgressState(progressState)
                    .subscribeOrToast { }
            })

        }
        TextColorDivider()
        HeightSpacer(height = 300.dp)
    }
}

@Preview
@Composable
fun GuidePagePreview() {
    GuideScreen()
}


@Preview
@Composable
fun FeatureGroupPreview() {
    WithDensity {
        FeatureGroup(deviceInfoState = +state<DeviceInfo?> { Mocks.MOCK_DEVICE_INFO })
    }
}