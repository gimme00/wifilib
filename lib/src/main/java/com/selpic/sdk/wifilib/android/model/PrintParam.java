package com.selpic.sdk.wifilib.android.model;

/** Printing parameter */
public class PrintParam {
    public static final PrintParam DEFAULT = new PrintParam(12, 2, 1);

    private final int PrtPlusWidth;
    private final int PrtGrayScale;
    private final int PrtVoltage;

    /**
     * @param prtPlusWidth Printing pulse width
     * @param prtGrayScale Printing gray level
     * @param prtVoltage   Printing voltage
     */
    public PrintParam(int prtPlusWidth, int prtGrayScale, int prtVoltage) {
        PrtPlusWidth = prtPlusWidth;
        PrtGrayScale = prtGrayScale;
        PrtVoltage = prtVoltage;
    }

    /** Printing pulse width */
    public int getPrtPlusWidth() {
        return PrtPlusWidth;
    }

    /** Printing gray level */
    public int getPrtGrayScale() {
        return PrtGrayScale;
    }

    /** Printing voltage */
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
