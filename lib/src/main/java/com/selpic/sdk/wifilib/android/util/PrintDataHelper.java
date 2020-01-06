package com.selpic.sdk.wifilib.android.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.selpic.sdk.wifilib.android.model.DeviceInfo;
import com.wzygswbxm.wifilib.comm.Bean.FileBeginBean;
import com.wzygswbxm.wifilib.comm.Bean.FileDetailBean;

import java.util.ArrayList;
import java.util.List;

import static com.wzygswbxm.wifilib.comm.Bean.FileBeginBean.GREY_TYPE_IMG_NOCHANGE;
import static com.wzygswbxm.wifilib.comm.Bean.FileBeginBean.TYPE_LEVE0;
import static com.wzygswbxm.wifilib.comm.Bean.FileBeginBean.TYPE_LEVE16;
import static com.wzygswbxm.wifilib.comm.Bean.FileBeginBean.TYPE_LEVE2;
import static com.wzygswbxm.wifilib.comm.util.ByteUtil.byte2OxString;
import static com.wzygswbxm.wifilib.comm.util.ByteUtil.int2OxString;

public class PrintDataHelper {
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素, 和ColorMatrix的灰度转换统一系数
                grey = (int) (red * 0.213f + green * 0.715f + blue * 0.072f);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
//                Log.d("zhou","grey="+Integer.toHexString( pixels[width * i + j] ));
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    private final DeviceInfo mDeviceInfo;

    public PrintDataHelper(DeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;
    }

    public FileBeginBean createBeginBean(int width, int height) {
        FileBeginBean beginBean = new FileBeginBean();
        beginBean.setBeginX(0);
        beginBean.setEndX(width - 1);
        beginBean.setBeginY(0);
        beginBean.setEndY(height - 1);
        beginBean.setGrayType(FileBeginBean.GREY_TYPE_IMG_NOCHANGE);
        return beginBean;
    }

    public FileDetailBean createDetailBean(int packageIndex, int beginX) {
        FileDetailBean detail = new FileDetailBean(mDeviceInfo.getStatus().getTransferMTU());
        detail.setPackageIndex(packageIndex);
        detail.setBeginY(0);
        detail.setEndY(mDeviceInfo.getPointPreColumn() - 1);
        detail.setBeginX(beginX);
        detail.setEndX(beginX);
        return detail;
    }

    public List<DownloadItem> bitmap2Files(Bitmap bitmap, List<FileBeginBean> list) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        int[][] pixels2 = new int[width][height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int row, column;
        for (int i = 0; i < pixels.length; i++) {
            row = (i) / width;
            column = i % width;
            pixels2[column][row] = pixels[i];
        }
        List<DownloadItem> result = new ArrayList<>();
        int offsetIndex = list.get(0).getBeginX();
        for (FileBeginBean begin : list) {
            List<FileDetailBean> details = new ArrayList<>();
            DownloadItem item = new DownloadItem(begin, details);
            result.add(item);
            int contentLength = 0;
            FileDetailBean detail = createDetailBean(0, offsetIndex);
            details.add(detail);
            byte[] columnByte;
            for (int x = begin.getBeginX() - offsetIndex; x <= begin.getEndX() - offsetIndex; x++) {
                if (x >= pixels2.length) break;
                columnByte = obtainDevBytesInserve(pixels2[x], begin.getGrayType());
                int pointX = x + offsetIndex;
                // 如果contentLength长度+这次的有效数据长度<detail.getContent.length 则继续往里面塞
                if (contentLength + columnByte.length < detail.getContent().length) {
                    detail.setEndX(pointX);
                } else {
                    // 否则就重新创建filedetailbean
                    detail.setTotalBytes(contentLength);
                    contentLength = 0;
                    detail = createDetailBean(details.size(), pointX);
                    details.add(detail);

                }
                System.arraycopy(columnByte, 0, detail.getContent(), contentLength, columnByte.length);
                contentLength += columnByte.length;
                detail.setTotalBytes(contentLength);
            }
        }
        // 给begin包填充总包数和总字节数数据
        for (DownloadItem item : result) {
            item.begin.setPackageNum(item.details.size());
            int totalBytes = 0;
            for (FileDetailBean detail : item.details) {
                totalBytes += detail.getTotalBytes();
            }
            item.begin.setTotalBytes(totalBytes);
        }
        return result;
    }

    private static final byte COLUMN_HEAD = 0x7a;

    private byte[] obtainDevBytesInserve(int[] pixels, int type) {
        byte[] result = null;
        if (type == GREY_TYPE_IMG_NOCHANGE) {
            result = new byte[(int) (Math.ceil(1.0 * mDeviceInfo.getPointPreColumn() / 8) + 3)];
            Log.v("zhou", "pixels.length=" + pixels.length);
            result[0] = COLUMN_HEAD;
            result[1] = (byte) type;
            result[2] = TYPE_LEVE2;

            int r;
            boolean isAllZero = true;
            for (int i = 0; i < pixels.length; i++) {
                // 由于之前已经将pixels处理成灰色, 故只要取blue通道的颜色就行了
                int blueColor = pixels[pixels.length - 1 - i] & 0x000000ff; // 范围[0, 255]
                r = 15 - blueColor / 16; // 范围[15, 0]
                // Log.v("zhou", "cloumn.r=" + r + " pixels[i]=" + pixels[i]);
                int byteIndex = i / 8 + 3; // `+ 3` 表示偏移掉最前面的3个字节
                // [0, 7], 不喷墨; [8, 15], 喷墨
                if (r >= 8) {
                    isAllZero = false;
                    result[byteIndex] |= 1 << (i % 8);
                }
            }
            if (isAllZero) {
                // 全为0时, 只发TYPE_LEVE0的头就行了
                result = new byte[]{COLUMN_HEAD, (byte) type, TYPE_LEVE0};
            }
        } else {
            result = new byte[(mDeviceInfo.getPointPreColumn() + 1) / 2 + 3];
            result[0] = COLUMN_HEAD;
            result[1] = (byte) type;
            result[2] = TYPE_LEVE16;
            int value = 0;
            int r = 0;
            for (int i = 0; i < pixels.length; i++) {
                r = (15 - ((pixels[pixels.length - 1 - i] & 0x000000f0) >> 4));
                if (r > 0) {
                    r = 4;
                }
                if (i % 2 == 0) {
                    result[i / 2 + 3] = (byte) (r & 0x0000000f);
                } else
                    result[i / 2 + 3] |= (r & 0x0000000f) * 16;

                if (i < 100) {
                    Log.d("zhou", "i=" + (pixels.length - 1 - i) + " cloumn.r=" + r + " pixels[i]=" + int2OxString(pixels[pixels.length - 1 - i])
                            + "   result[i/2+3]" + byte2OxString(result[i / 2 + 3])
                            + " (pixels[pixels.length-1-i]&0x000000ff)/16=" + ((pixels[pixels.length - 1 - i] & 0x000000f0) >> 4)
                    );
                }
            }
        }
        return result;
    }

    public static class DownloadItem {
        public final FileBeginBean begin;
        public final List<FileDetailBean> details;

        public DownloadItem(FileBeginBean begin, List<FileDetailBean> details) {
            this.begin = begin;
            this.details = details;
        }
    }
}
