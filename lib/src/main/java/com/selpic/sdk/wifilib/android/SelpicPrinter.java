package com.selpic.sdk.wifilib.android;

import android.graphics.Bitmap;

import com.selpic.sdk.wifilib.android.model.DeviceInfo;
import com.selpic.sdk.wifilib.android.model.PacketType;
import com.selpic.sdk.wifilib.android.model.PrintParam;
import com.selpic.sdk.wifilib.android.model.VirtualFile;

import io.reactivex.Observable;
import io.reactivex.Single;

/** 打印机的功能接口 */
public interface SelpicPrinter {
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
    Observable<PacketType> sendOta(VirtualFile file, int versionCode);

    /**
     * 发送OTA的初始打印数据
     * @param file
     * @return 订阅后可以获取发送的进度
     */
    Observable<PacketType> sendPrintDataOta(VirtualFile file);

    /**
     * 发送打印数据
     * @param bitmap Bitmap形式的打印数据, 必须保证它的高度等于{@link DeviceInfo#getPointPreColumn()}
     * @return 订阅后可以获取发送的进度
     */
    Observable<PacketType> sendPrintData(Bitmap bitmap);
}
