package com.selpic.sdk.wifilib.android.model;

import java.io.InputStream;

/** 虚拟文件 */
public interface VirtualFile {
    /** 通过流读取文件内容 */
    InputStream inputStream();
}
