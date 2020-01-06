package com.selpic.wifilib.sample.ui.guide

import android.util.Log
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.memo
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.core.gesture.DragObserver
import androidx.ui.core.gesture.TouchSlopDragGestureDetector
import androidx.ui.engine.geometry.Shape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.foundation.shape.border.Border
import androidx.ui.layout.ExpandedWidth
import androidx.ui.layout.Height
import androidx.ui.layout.Stack
import androidx.ui.material.surface.Surface
import com.selpic.wifilib.sample.ktx.ComposableFunction

@Model
data class PrintPreviewItemModel(
    var x: Dp = 0.dp,
    var y: Dp = 0.dp,
    var isCanDrag: Boolean = true
)

/**
 * @see androidx.ui.layout.StackChildren
 */
class PrintPreviewChildren {
    val printPreviewChildren = mutableListOf<Pair<PrintPreviewItemModel, ComposableFunction>>()
    fun item(
        model: PrintPreviewItemModel = +memo { PrintPreviewItemModel() },
        child: ComposableFunction
    ) {
        printPreviewChildren += model to child
    }
}

@Composable
fun PrintPreview(
    pointPreColumn: Int,
    onDragStart: () -> Unit = {},
    modifier: Modifier = Modifier.None,
    shape: Shape = RectangleShape,
    border: Border? = null,
    children: PrintPreviewChildren.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        border = border
    ) {
        WithDensity {
            Stack(modifier = Height(pointPreColumn.toDp()) wraps ExpandedWidth) {
                with(PrintPreviewChildren()) {
                    // collect item to printPreviewChildren
                    children()
                    for ((model, child) in printPreviewChildren) {
                        Log.d("PrintPreview", "PrintPreview: $model")
                        positioned(leftInset = model.x, topInset = model.y) {
                            TouchSlopDragGestureDetector(
                                dragObserver = object : DragObserver {
                                    override fun onStart(downPosition: PxPosition) {
                                        // super.onStart(downPosition)
                                        onDragStart()
                                    }

                                    override fun onDrag(dragDistance: PxPosition): PxPosition {
                                        model.x += dragDistance.x.toDp()
                                        model.y += dragDistance.y.toDp()
                                        return dragDistance
                                    }
                                },
                                canDrag = { model.isCanDrag },
                                children = child
                            )
                        }
                    }
                }
            }
        }
    }
}