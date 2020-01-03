package com.selpic.wifilib.sample.util

import android.content.res.AssetManager
import com.selpic.sdk.wifilib.android.model.VirtualFile
import java.io.InputStream

class AssertFile(val path: String, private val am: AssetManager) : VirtualFile {
    override fun inputStream(): InputStream = am.open(path)
}