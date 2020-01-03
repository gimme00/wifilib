package com.selpic.sdk.wifilib.android.model;

import com.wzygswbxm.wifilib.comm.Bean.DevStatusBean;

/** 设备信息 */
public class DeviceInfo {
    private DevStatusBean mStatus;

    public DeviceInfo(DevStatusBean mStatus) {
        this.mStatus = mStatus;
    }

    /** 类型名 */
    public String getTypeName() {
        return mStatus.getClassName();
    }

    /** 版本号 */
    public int getVersionCode() {
        return mStatus.getVersionCode();
    }

    /** 电量百分比 */
    public float getBatteryPercentage() {
        return mStatus.getBatteryCapacity() / 100f;
    }

    /** 充电中 */
    public boolean isCharging() {
        return mStatus.getChargeState() == 1;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "typeName=" + getTypeName() +
                ", versionCode=" + getVersionCode() +
                ", batteryPercentage=" + getBatteryPercentage() +
                ", isCharging=" + isCharging() +
                '}';
    }
}
