package com.selpic.sdk.wifilib.android;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.selpic.sdk.wifilib.android.model.DeviceInfo;
import com.selpic.sdk.wifilib.android.model.SendStatus;
import com.selpic.sdk.wifilib.android.model.PrintParam;
import com.selpic.sdk.wifilib.android.model.VirtualFile;
import com.wzygswbxm.wifilib.comm.Bean.DevStatusBean;
import com.wzygswbxm.wifilib.comm.WifiModuleContext;
import com.wzygswbxm.wifilib.comm.queue.MyPrintTcpQueueHelper;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface SelpicPrinter {
    static SelpicPrinter create(Context context) {
        // init wifi-core
        WifiModuleContext.setAppContext(context);
        MyPrintTcpQueueHelper.setmMyPrintTcpQueueHelper(new MyPrintTcpQueueHelper((Application) context.getApplicationContext()));
        return new SelpicPrinterImpl(context);
    }

    /**
     * 获取设备信息
     * @return
     */
    Single<DeviceInfo> getDeviceInfo();

    /**
     * 实时接收设备信息
     * @return
     */
    Observable<DeviceInfo> receiveDeviceInfo();

    /**
     * 设置打印参数
     * @param printParam
     * @return
     */
    Single<?> setPrintParam(PrintParam printParam);

    /**
     * 发送OTA数据
     * @param file
     * @return 订阅后可以获取发送的进度
     */
    Observable<SendStatus> sendOta(VirtualFile file);

    /**
     * 发送OTA的初始打印数据
     * @param file
     * @return 订阅后可以获取发送的进度
     */
    Observable<SendStatus> sendPrintDataOta(VirtualFile file);

    /**
     * 发送打印数据
     * @param bitmap Bitmap形式的打印数据
     * @return 订阅后可以获取发送的进度
     */
    Observable<SendStatus> sendPrintData(Bitmap bitmap);
}
