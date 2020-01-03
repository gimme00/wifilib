package com.selpic.wifilib.sample.ui.widget

import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.effectOf
import androidx.compose.unaryPlus
import androidx.ui.core.Dp
import androidx.ui.core.Modifier
import androidx.ui.core.currentTextStyle
import androidx.ui.core.dp
import androidx.ui.graphics.Color
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import com.selpic.wifilib.sample.ktx.withOpacity


fun currentTextColor() = effectOf<Color> {
    (+currentTextStyle()).color ?: (+MaterialTheme.colors()).onSurface
}

@Composable
fun TextColorDivider(
    modifier: Modifier = Modifier.None,
    color: Color = (+currentTextColor()).withOpacity(0.1f),
    height: Dp = 1.dp,
    indent: Dp = 0.dp
) = Divider(modifier, color, height, indent)