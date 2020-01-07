package com.selpic.sdk.wifilib.android.model;

/** 发送数据包的类型 */
public interface PacketType {
    /** 开始包 */
    class Start implements PacketType {
        public static final Start INSTANCE = new Start();
    }

    /** 子包 */
    class Sub implements PacketType {
        private final int mIndex;
        private final int mTotal;

        public Sub(int index, int total) {
            mIndex = index;
            mTotal = total;
        }

        /** 第几个包 */
        public int getIndex() {
            return mIndex;
        }

        /** 总包数 */
        public int getTotal() {
            return mTotal;
        }
    }

    /** 其他包 */
    class Other implements PacketType {
        public static final Other INSTANCE = new Other();
    }

    /** 结束包 */
    class End implements PacketType {
        public static final End INSTANCE = new End();
    }
}
