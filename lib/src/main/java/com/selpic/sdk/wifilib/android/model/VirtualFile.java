package com.selpic.sdk.wifilib.android.model;

import java.io.InputStream;

/** Virtual file */
public interface VirtualFile {
    /** Read file content using stream */
    InputStream inputStream();
}
