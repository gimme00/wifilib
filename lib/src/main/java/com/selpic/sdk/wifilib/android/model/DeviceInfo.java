package com.selpic.sdk.wifilib.android.model;

import com.selpic.sdk.wifilib.android.util.Hide;
import com.wzygswbxm.wifilib.comm.Bean.DevStatusBean;

/** 设备信息 */
public class DeviceInfo {
    public static final String TYPE_NAME_S1 = "1.0";
    public static final String TYPE_NAME_S1_PLUS = "1.1";
    public static final String TYPE_NAME_P1 = "1.2";

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

    @Hide
    public DevStatusBean getStatus() {
        return mStatus;
    }

    /** 每列的点数 */
    public int getPointPreColumn() {
        return mStatus.getPointPreColumn();
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "typeName=" + getTypeName() +
                ", versionCode=" + getVersionCode() +
                ", batteryPercentage=" + getBatteryPercentage() +
                ", isCharging=" + isCharging() +
                ", pointPreColumn=" + getPointPreColumn() +
                '}';
    }
}
