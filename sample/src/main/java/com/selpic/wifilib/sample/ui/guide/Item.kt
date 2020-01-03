package com.selpic.wifilib.sample.ui.guide

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.layout.Column
import androidx.ui.layout.Padding
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.withOpacity
import androidx.ui.tooling.preview.Preview
import com.selpic.wifilib.sample.ktx.ComposableFuncation
import com.selpic.wifilib.sample.ui.emphasisHighTypeOpacity
import com.selpic.wifilib.sample.ui.emphasisMediumOpacity

@Composable
fun Item(
    title: String="",
    summary: String? = null,
    children: ComposableFuncation
) {
    Padding(padding = 8.dp) {
        Column {
            val typography = +MaterialTheme.typography()
            Text(
                title, style = typography.h5.withOpacity(
                    emphasisHighTypeOpacity
                )
            )
            if (summary != null) {
                Text(summary, style = typography.body2.withOpacity(emphasisMediumOpacity))
            }
            children()
        }
    }
}

@Composable
@Preview
fun ItemPreview() {
    Item(
        title = "title",
        summary = "summary"
    ) {
        Button("Button")
    }
}