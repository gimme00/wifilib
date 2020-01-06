package com.selpic.wifilib.sample.ui.widget

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Dp
import androidx.ui.core.Modifier
import androidx.ui.core.dp
import androidx.ui.graphics.Color
import androidx.ui.material.Divider
import com.selpic.wifilib.sample.ktx.currentTextColor
import com.selpic.wifilib.sample.ktx.withOpacity
import com.selpic.wifilib.sample.ui.dividerOpacity


@Composable
fun TextColorDivider(
    modifier: Modifier = Modifier.None,
    color: Color = (+currentTextColor()).withOpacity(dividerOpacity),
    height: Dp = 1.dp,
    indent: Dp = 0.dp
) = Divider(modifier, color, height, indent)