package com.selpic.sdk.wifilib.android.model;

/** 打印参数 */
public class PrintParam {
    public static final PrintParam DEFAULT = new PrintParam(12, 2, 1);

    private final int PrtPlusWidth;
    private final int PrtGrayScale;
    private final int PrtVoltage;

    public PrintParam(int prtPlusWidth, int prtGrayScale, int prtVoltage) {
        PrtPlusWidth = prtPlusWidth;
        PrtGrayScale = prtGrayScale;
        PrtVoltage = prtVoltage;
    }

    public int getPrtPlusWidth() {
        return PrtPlusWidth;
    }

    public int getPrtGrayScale() {
        return PrtGrayScale;
    }

    public int getPrtVoltage() {
        return PrtVoltage;
    }

    @Override
    public String toString() {
        return "PrintParam{" +
                "PrtPlusWidth=" + PrtPlusWidth +
                ", PrtGrayScale=" + PrtGrayScale +
                ", PrtVoltage=" + PrtVoltage +
                '}';
    }
}
