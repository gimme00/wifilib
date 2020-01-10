package com.selpic.sdk.wifilib.android.util;

import com.selpic.sdk.wifilib.android.model.DeviceInfo;

import androidx.annotation.VisibleForTesting;

/** for test use only */
@VisibleForTesting
public class Mocks {
    public static DeviceInfo MOCK_DEVICE_INFO = new DeviceInfo(null) {
        @Override
        public String getTypeName() {
            return "1.1";
        }

        @Override
        public int getVersionCode() {
            return 1;
        }

        @Override
        public float getBatteryPercentage() {
            return 0.5f;
        }

        @Override
        public boolean isCharging() {
            return false;
        }

        @Override
        public int getPointPreColumn() {
            return 150;
        }
    };
}
