package com.selpic.sdk.wifilib.android.model;

/** 打印参数 */
public class PrintParam {
    public static final PrintParam DEFAULT = new PrintParam(12, 2, 1);

    private final int PrtPlusWidth;
    private final int PrtGrayScale;
    private final int PrtVoltage;

    /**
     * @param prtPlusWidth 打印脉冲宽度
     * @param prtGrayScale 打印灰度
     * @param prtVoltage   打印电压
     */
    public PrintParam(int prtPlusWidth, int prtGrayScale, int prtVoltage) {
        PrtPlusWidth = prtPlusWidth;
        PrtGrayScale = prtGrayScale;
        PrtVoltage = prtVoltage;
    }

    /** 打印脉冲宽度 */
    public int getPrtPlusWidth() {
        return PrtPlusWidth;
    }

    /** 打印灰度 */
    public int getPrtGrayScale() {
        return PrtGrayScale;
    }

    /** 打印电压 */
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
