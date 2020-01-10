package com.selpic.sdk.wifilib.android.model;

import com.selpic.sdk.wifilib.android.util.Hide;
import com.wzygswbxm.wifilib.comm.Bean.DevStatusBean;

/** Device information */
public class DeviceInfo {
    public static final String TYPE_NAME_S1 = "1.0";
    public static final String TYPE_NAME_S1_PLUS = "1.1";
    public static final String TYPE_NAME_P1 = "1.2";

    private DevStatusBean mStatus;

    public DeviceInfo(DevStatusBean mStatus) {
        this.mStatus = mStatus;
    }

    /** Typename */
    public String getTypeName() {
        return mStatus.getClassName();
    }

    /** Version code */
    public int getVersionCode() {
        return mStatus.getVersionCode();
    }

    /** Battery percentage */
    public float getBatteryPercentage() {
        return mStatus.getBatteryCapacity() / 100f;
    }

    /** Charging */
    public boolean isCharging() {
        return mStatus.getChargeState() == 1;
    }

    @Hide
    public DevStatusBean getStatus() {
        return mStatus;
    }

    /** Points per column */
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
