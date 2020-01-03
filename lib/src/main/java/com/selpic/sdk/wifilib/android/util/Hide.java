package com.selpic.sdk.wifilib.android.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** 注解lib内部使用的方法, 外部不应该调用 */
@Retention(RetentionPolicy.SOURCE)
public @interface Hide {
}
