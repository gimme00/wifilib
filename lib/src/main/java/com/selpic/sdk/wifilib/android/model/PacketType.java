package com.selpic.sdk.wifilib.android.model;

/** Type of package you're sending */
public interface PacketType {
    /** Start package */
    class Start implements PacketType {
        public static final Start INSTANCE = new Start();
    }

    /** Subpackage */
    class Sub implements PacketType {
        private final int mIndex;
        private final int mTotal;

        public Sub(int index, int total) {
            mIndex = index;
            mTotal = total;
        }

        /** Which package */
        public int getIndex() {
            return mIndex;
        }

        /** Total number of package */
        public int getTotal() {
            return mTotal;
        }
    }

    /** Other package */
    class Other implements PacketType {
        public static final Other INSTANCE = new Other();
    }

    /** End package */
    class End implements PacketType {
        public static final End INSTANCE = new End();
    }
}
