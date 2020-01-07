package com.selpic.wifilib.sample.ktx

import android.graphics.Camera
import android.graphics.Matrix

fun Camera.toMatrix(): Matrix {
    val matrix = Matrix()
    this.getMatrix(matrix)
    return matrix
}