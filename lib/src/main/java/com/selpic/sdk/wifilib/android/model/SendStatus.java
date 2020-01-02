package com.selpic.sdk.wifilib.android.model;

/** 发送数据的状态 */
public interface SendStatus {
    /** 开始 */
    class Start implements SendStatus {}

    /** 发送中, 通过{@link #getPercent()}获取进度 */
    class Progress implements SendStatus {
        private final float mPercent;

        public Progress(float percent) {
            mPercent = percent;
        }

        public float getPercent() {
            return mPercent;
        }
    }

    /** 发送中, 未知进度 */
    class UnknownProgress implements SendStatus {}

    /** 结束 */
    class End implements SendStatus {}
}
