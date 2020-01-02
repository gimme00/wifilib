package com.selpic.sdk.wifilib.android;

import android.content.Context;
import android.graphics.Bitmap;

import com.selpic.sdk.wifilib.android.model.SendStatus;
import com.selpic.sdk.wifilib.android.model.PrintParam;
import com.selpic.sdk.wifilib.android.model.VirtualFile;
import com.wzygswbxm.wifilib.comm.Bean.DevStatusBean;
import com.wzygswbxm.wifilib.comm.Bean.PrintParams;
import com.wzygswbxm.wifilib.comm.IPrinterHelper;
import com.wzygswbxm.wifilib.comm.util.ByteUtil;

import io.reactivex.Observable;
import io.reactivex.Single;

/*package*/ class SelpicPrinterImpl implements SelpicPrinter {
    private Context mContext;

    public SelpicPrinterImpl(Context context) {
        mContext = context;
    }

    @Override
    public Single<DevStatusBean> getDevStatus() {
        return IPrinterHelper.get().checkDevStatus().map(it -> it.getResult()).singleOrError();
    }

    @Override
    public Observable<DevStatusBean> receiveDevStatus() {
        return IPrinterHelper.get().recvDevStatus().map(it -> it.getResult());
    }

    @Override
    public Single<?> setPrintParam(PrintParam printParam) {
        PrintParams params = new PrintParams();
        // use default value
        params.setPrtBeginDelay(10);
        params.setPrtColsMotos(1);
        params.setPrtRorL(1);
        params.setIdlePrt(0);
        params.setPttRasterize(0);
        // use printParam
        params.setPrtPlusWidth(printParam.getPrtPlusWidth());
        params.setPrtGrayScale(printParam.getPrtGrayScale());
        params.setPrtvoltage(printParam.getPrtVoltage());
        return IPrinterHelper.get().setPrintParams(params).singleOrError();
    }

    @Override
    public Observable<SendStatus> sendOta(VirtualFile file) {
        // TODO:
        return null;
    }

    @Override
    public Observable<SendStatus> sendPrintDataOta(VirtualFile file) {
        return null;
    }

    @Override
    public Observable<SendStatus> sendPrintData(Bitmap bitmap) {
        return null;
    }
}
