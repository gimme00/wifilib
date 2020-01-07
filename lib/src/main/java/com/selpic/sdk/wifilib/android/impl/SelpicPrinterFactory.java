package com.selpic.sdk.wifilib.android.impl;

import android.app.Application;
import android.content.Context;

import com.selpic.sdk.wifilib.android.SelpicPrinter;
import com.wzygswbxm.wifilib.comm.WifiModuleContext;
import com.wzygswbxm.wifilib.comm.queue.MyPrintTcpQueueHelper;

public class SelpicPrinterFactory {
    /** 创建{@link SelpicPrinter}实例 */
    public static SelpicPrinter create(Context context) {
        // init wifi-core
        WifiModuleContext.setAppContext(context);
        MyPrintTcpQueueHelper.setmMyPrintTcpQueueHelper(new MyPrintTcpQueueHelper((Application) context.getApplicationContext()));
        return new SelpicPrinterImpl(context);
    }
}
