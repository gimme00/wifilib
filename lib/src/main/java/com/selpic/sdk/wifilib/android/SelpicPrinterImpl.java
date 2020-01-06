package com.selpic.sdk.wifilib.android;

import android.content.Context;
import android.graphics.Bitmap;

import com.selpic.sdk.wifilib.android.model.DeviceInfo;
import com.selpic.sdk.wifilib.android.model.PacketType;
import com.selpic.sdk.wifilib.android.model.PrintParam;
import com.selpic.sdk.wifilib.android.model.VirtualFile;
import com.selpic.sdk.wifilib.android.util.ByteUtil;
import com.selpic.sdk.wifilib.android.util.OtaHelper;
import com.selpic.sdk.wifilib.android.util.PrintDataHelper;
import com.wzygswbxm.wifilib.comm.Bean.DevStatusBean;
import com.wzygswbxm.wifilib.comm.Bean.DownLoadBegin;
import com.wzygswbxm.wifilib.comm.Bean.DownLoadEnd;
import com.wzygswbxm.wifilib.comm.Bean.FileBeginBean;
import com.wzygswbxm.wifilib.comm.Bean.FileDetailBean;
import com.wzygswbxm.wifilib.comm.Bean.OtaBean;
import com.wzygswbxm.wifilib.comm.Bean.OtaBeginBean;
import com.wzygswbxm.wifilib.comm.Bean.OtaEndBean;
import com.wzygswbxm.wifilib.comm.Bean.PrintParams;
import com.wzygswbxm.wifilib.comm.Bean.ResultCodeBean;
import com.wzygswbxm.wifilib.comm.IPrinter;
import com.wzygswbxm.wifilib.comm.IPrinterHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*package*/ class SelpicPrinterImpl implements SelpicPrinter {
    private Context mContext;
    private volatile DeviceInfo mDeviceInfo;

    public SelpicPrinterImpl(Context context) {
        mContext = context;
    }

    @Override
    public Single<DeviceInfo> getDeviceInfo() {
        return IPrinterHelper.get().checkDevStatus().map(it -> {
            DeviceInfo deviceInfo = new DeviceInfo(it.getResult());
            mDeviceInfo = deviceInfo;
            return deviceInfo;
        }).singleOrError();
    }

    @Override
    public Observable<DeviceInfo> receiveDeviceInfo() {
        return IPrinterHelper.get().recvDevStatus().map(it -> {
            DeviceInfo deviceInfo = new DeviceInfo(it.getResult());
            mDeviceInfo = deviceInfo;
            return deviceInfo;
        });
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

    private Single<DeviceInfo> ensureDeviceInfo() {
        DeviceInfo deviceInfo = mDeviceInfo;
        return deviceInfo != null ? Single.just(deviceInfo) : getDeviceInfo();
    }

    private Observable<PacketType> sendOtaImpl(OtaHelper ota, VirtualFile file, int versionCode) {
        return ensureDeviceInfo().flatMapObservable(info -> {
            byte[] bytes = ByteUtil.readBytes(file.inputStream());
            DevStatusBean status = info.getStatus();

            OtaBeginBean beginBean = ota.createOtaBeginPacket(bytes, status.getTransferMTU(), status.getClassValue(), versionCode);
            List<OtaBean> beans = ota.createOtaPacket(bytes, status.getTransferMTU(), beginBean);
            OtaEndBean endBean = ota.createOtaEndPacket();

            List<Observable<PacketType>> tasks = new ArrayList<>();
            tasks.add(ota.otaBeginSend(beginBean).map(it -> PacketType.Start.INSTANCE));
            for (int i = 0; i < beans.size(); i++) {
                OtaBean bean = beans.get(i);
                final int index = i;
                tasks.add(ota.otaSend(bean).map(it -> new PacketType.Sub(index, beans.size())));
            }
            tasks.add(ota.otaEndSend(endBean).map(it -> PacketType.End.INSTANCE));
            return Observable.concat(tasks);
        })
                // io -> main
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PacketType> sendOta(VirtualFile file, int versionCode) {
        return sendOtaImpl(new OtaHelper.Dev(), file, versionCode).doOnComplete(() -> {
            Disposable disposable = IPrinterHelper.get().otaReboot().subscribe(
                    it -> {
                        // pass
                    },
                    e -> {
                        // pass
                    }
            );
        });
    }

    @Override
    public Observable<PacketType> sendPrintDataOta(VirtualFile file) {
        return sendOtaImpl(new OtaHelper.PrintData(), file, 0);
    }

    @Override
    public Observable<PacketType> sendPrintData(Bitmap bitmap) {
        DeviceInfo deviceInfo = mDeviceInfo;
        int pointPreColumn = deviceInfo.getPointPreColumn();
        if (bitmap.getHeight() != pointPreColumn) {
            throw new IllegalArgumentException("bitmap.getHeight() must equal to pointPreColumn");
        }
        return Observable.just("")
                .flatMap(it -> {
                    PrintDataHelper helper = new PrintDataHelper(deviceInfo);
                    IPrinter printer = IPrinterHelper.get();

                    Bitmap grayedBitmap = PrintDataHelper.convertToBlackWhite(bitmap);
                    FileBeginBean begin = helper.createBeginBean(grayedBitmap.getWidth(), grayedBitmap.getHeight());
                    List<PrintDataHelper.DownloadItem> downloadItems = helper.bitmap2Files(grayedBitmap, Collections.singletonList(begin));

                    List<Observable<PacketType>> tasks = new ArrayList<>();
                    tasks.add(printer.downLoadBegin(new DownLoadBegin(0)).map(__ -> PacketType.Start.INSTANCE));
                    for (PrintDataHelper.DownloadItem item : downloadItems) {
                        tasks.add(printer.downLoadFileBegin(item.begin).map(__ -> PacketType.Other.INSTANCE));
                        for (FileDetailBean detail : item.details) {
                            tasks.add(printer.downLoadFileDetail(detail).compose(ResultCodeBean.convertResultCodeToException()).map(__ -> new PacketType.Sub(detail.getPackageIndex(), item.begin.getPackageNum())));
                        }
                        tasks.add(printer.downLoadFileEnd().map(__ -> PacketType.Other.INSTANCE));
                    }
                    tasks.add(printer.downLoadEnd(new DownLoadEnd()).map(itt -> PacketType.End.INSTANCE));

                    return Observable.concat(tasks)
                            .onErrorResumeNext(throwable -> {
                                if (throwable instanceof ResultCodeBean.Exception && ((ResultCodeBean.Exception) throwable).getCode() == ResultCodeBean.CODE_DEV_STORAGE_FULL) {
                                    return Observable.concat(
                                            printer.downLoadFileEnd().map(__ -> PacketType.Other.INSTANCE),
                                            printer.downLoadEnd(new DownLoadEnd()).map(__ -> PacketType.End.INSTANCE)
                                    );
                                }
                                return Observable.error(throwable);
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
