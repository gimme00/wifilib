package com.selpic.sdk.wifilib.android.util;

import com.wzygswbxm.wifilib.comm.Bean.OtaBean;
import com.wzygswbxm.wifilib.comm.Bean.OtaBeginBean;
import com.wzygswbxm.wifilib.comm.Bean.OtaEndBean;
import com.wzygswbxm.wifilib.comm.IPrinterHelper;
import com.wzygswbxm.wifilib.comm.ProcessPub;
import com.wzygswbxm.wifilib.comm.processer.Process_COMM_FUNCTION_OTA;
import com.wzygswbxm.wifilib.comm.processer.Process_COMM_FUNCTION_OTA_BEGIN;
import com.wzygswbxm.wifilib.comm.processer.Process_COMM_FUNCTION_OTA_END;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static com.wzygswbxm.wifilib.comm.PRINT_CONST.RETRY_TIMES;
import static com.wzygswbxm.wifilib.comm.util.ByteUtil.byte2ToInt;
import static com.wzygswbxm.wifilib.comm.util.ByteUtil.intToByte4;
import static com.wzygswbxm.wifilib.comm.util.CRC16Utils.obtainCRC16;

abstract public class OtaHelper {
    public OtaBeginBean createOtaBeginPacket(
            byte[] otabytes,
            int transferMTU,
            int classValue,
            int versionCode
    ) {
        OtaBeginBean otaBeginBean = new OtaBeginBean();
        otaBeginBean.setTotalBytes(otabytes.length);
        otaBeginBean.setMtu(transferMTU);
        // 剩下的参数只有设备OTA升级时有效, 下发打印数据时不会使用
        int mylength = otabytes.length / transferMTU + (otabytes.length % transferMTU > 0 ? 1 : 0);
        otaBeginBean.setTotalPackageNum(mylength);
        otaBeginBean.setClassValue(classValue);
        otaBeginBean.setVersionIndex(versionCode);
        return otaBeginBean;
    }

    public List<OtaBean> createOtaPacket(
            byte[] otabytes,
            int transferMTU,
            OtaBeginBean outBeginBean
    ) {
        List<OtaBean> list = new ArrayList<>();
        byte[] content;
        int offset = 0;
        int packetCount = (int) Math.ceil(1.0 * otabytes.length / transferMTU);
        int crcNum = 0;
        for (int i = 0; i < packetCount; i++) {
            if (i == packetCount - 1) {
                content = new byte[otabytes.length - offset];
            } else {
                content = new byte[transferMTU];
            }
            System.arraycopy(otabytes, offset, content, 0, content.length);
            OtaBean otaBean = new OtaBean();

            otaBean.setIndexOfPackageNum(i);
            otaBean.setEffectiveNum(content.length);
            otaBean.setContent(content);
            otaBean.setCrcCurrent(obtainCRC16(content));
            list.add(otaBean);
            offset += content.length;
            crcNum += byte2ToInt(otaBean.getCrcCurrent());
        }
        outBeginBean.setCrcAllContent(intToByte4(crcNum));
        return list;
    }

    public OtaEndBean createOtaEndPacket() {
        return new OtaEndBean();
    }

    abstract public Observable<? extends Process_COMM_FUNCTION_OTA_BEGIN> otaBeginSend(OtaBeginBean beginBean);

    abstract public Observable<? extends Process_COMM_FUNCTION_OTA> otaSend(OtaBean beginBean);

    abstract public Observable<? extends Process_COMM_FUNCTION_OTA_END> otaEndSend(OtaEndBean endBean);

    public static class Dev extends OtaHelper {
        public Observable<? extends Process_COMM_FUNCTION_OTA_BEGIN> otaBeginSend(OtaBeginBean beginBean) {
            return IPrinterHelper.get().otaBeginSend(beginBean);
        }

        public Observable<? extends Process_COMM_FUNCTION_OTA> otaSend(OtaBean beginBean) {
            return IPrinterHelper.get().otaSend(beginBean);
        }

        public Observable<? extends Process_COMM_FUNCTION_OTA_END> otaEndSend(OtaEndBean endBean) {
            return IPrinterHelper.get().otaEndSend(endBean);
        }
    }
    public static class PrintData extends OtaHelper {
        public Observable<? extends Process_COMM_FUNCTION_OTA_BEGIN> otaBeginSend(OtaBeginBean beginBean) {
            return IPrinterHelper.get().printDataOtaBeginSend(beginBean);
        }

        public Observable<? extends Process_COMM_FUNCTION_OTA> otaSend(OtaBean beginBean) {
            return IPrinterHelper.get().printDataOtaSend(beginBean);
        }

        public Observable<? extends Process_COMM_FUNCTION_OTA_END> otaEndSend(OtaEndBean endBean) {
            return IPrinterHelper.get().printDataOtaEndSend(endBean);
        }
    }
}
