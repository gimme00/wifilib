package com.selpic.wifilib.sample.ui.guide

import androidx.compose.Composable
import androidx.compose.State
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.foundation.shape.border.Border
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.surface.Surface
import androidx.ui.material.withOpacity
import androidx.ui.text.style.TextOverflow
import androidx.ui.tooling.preview.Preview
import com.selpic.wifilib.sample.ktx.withOpacity
import com.selpic.wifilib.sample.ui.emphasisDisabledOpacity
import com.selpic.wifilib.sample.ui.emphasisMediumOpacity
import com.selpic.wifilib.sample.ui.widget.currentTextColor

@Composable
fun IntTextInput(
        value: Int?,
        onValueChange: (Int?) -> Unit = {},
        name: String,
        defaultValue: Int,
        hint: String = "",
        modifier: Modifier = Modifier.None
) {
    TextInput(
            value = value?.toString() ?: "",
            onValueChange = { it.toIntOrNull()?.let(onValueChange) },
            name = name,
            defaultValue = defaultValue,
            modifier = modifier,
            hint = hint
    )
}

@Composable
fun TextInput(
        value: String,
        onValueChange: (String) -> Unit = {},
        name: String,
        defaultValue: Int,
        hint: String = "",
        modifier: Modifier = Modifier.None
) {
    Surface(
            modifier = modifier,
            shape = RoundedCornerShape(50),
            border = Border((+currentTextColor()).withOpacity(0.1f), 1.dp)
    ) {
        Padding(left = 16.dp, top = 8.dp, right = 16.dp, bottom = 8.dp) {
            Row {
                Text(
                        text = name,
                        modifier = MinWidth(100.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = (+currentTextStyle()).withOpacity(
                                emphasisMediumOpacity
                        )
                )
                Stack {
                    aligned(Alignment.BottomLeft) {
                        if (value.isEmpty()) {
                            Text(
                                    text = "$hint default=$defaultValue",
                                    style = (+currentTextStyle()).withOpacity(
                                            emphasisDisabledOpacity
                                    )
                            )
                        }
                        TextField(
                                textStyle = (+currentTextStyle()).withOpacity(
                                        emphasisMediumOpacity
                                ),
                                modifier = ExpandedWidth,
                                keyboardType = KeyboardType.Number,
                                value = value,
                                onValueChange = onValueChange
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun ParamIntTextFieldPreview() {
    IntTextInput(value = 1, name = "Name", defaultValue = 1, hint = "")
}