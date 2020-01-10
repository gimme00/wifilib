package com.selpic.sdk.wifilib.android;

import android.graphics.Bitmap;

import com.selpic.sdk.wifilib.android.model.DeviceInfo;
import com.selpic.sdk.wifilib.android.model.PacketType;
import com.selpic.sdk.wifilib.android.model.PrintParam;
import com.selpic.sdk.wifilib.android.model.VirtualFile;

import io.reactivex.Observable;
import io.reactivex.Single;

/** Functional interface of printer */
public interface SelpicPrinter {
    /**
     * Get device information
     * @return
     */
    Single<DeviceInfo> getDeviceInfo();

    /**
     * Receive device information in real time
     * @return
     */
    Observable<DeviceInfo> receiveDeviceInfo();

    /**
     * Set up print parameters
     * @param printParam
     * @return
     */
    Single<?> setPrintParam(PrintParam printParam);

    /**
     * Send OTA data
     * @param file
     * @return Get sending progress after subscription
     */
    Observable<PacketType> sendOta(VirtualFile file, int versionCode);

    /**
     * Send the original print data of OTA
     * @param file
     * @return Get sending progress after subscription
     */
    Observable<PacketType> sendPrintDataOta(VirtualFile file);

    /**
     * Send print data
     * @param bitmap Print data in Bitmap format, and its height must be {@link DeviceInfo#getPointPreColumn()}
     * @return Get sending progress after subscription
     */
    Observable<PacketType> sendPrintData(Bitmap bitmap);
}
