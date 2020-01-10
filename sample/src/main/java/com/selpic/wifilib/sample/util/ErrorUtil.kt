package com.selpic.wifilib.sample.util

import android.content.Context
import android.util.Log
import com.selpic.wifilib.sample.App
import com.selpic.wifilib.sample.ktx.toast

object ErrorUtil {
    private const val TAG = "ErrorUtil"
    /**
     * @return return "", indicating that toast should not be displayed; return null, indicates an unknown error;
     */
    fun getMessage(context: Context, e: Throwable): String? {
        return when (e) {
            else -> e.message
        }
    }

    fun toast(e: Throwable) =
        toast(App.appContext, e)

    fun toast(context: Context, e: Throwable) {
        val message = getMessage(
            context,
            e
        ) ?: "Unknown error"
        if (message.isNotEmpty()) {
            context.toast(message)
        }
        e.printStackTrace()
    }

    fun log(e: Throwable) {
        Log.w(TAG, e.toString())
        e.printStackTrace()
    }
}