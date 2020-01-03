package com.selpic.sdk.wifilib.android.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteUtil {
    public static int copyTo(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int len;
        int totalLen = 0;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
            totalLen += len;
        }
        return totalLen;
    }

    public static byte[] readBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(is.available());
        copyTo(is, bos);
        return bos.toByteArray();
    }
}
