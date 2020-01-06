package com.selpic.wifilib.sample.ktx

import com.selpic.wifilib.sample.util.ErrorUtil
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.subscribeOrLog(onNext: (T) -> Unit): Disposable = this.subscribe(
    onNext,
    ErrorUtil::log
)

fun <T> Observable<T>.subscribeOrToast(onNext: (T) -> Unit): Disposable = this.subscribe(
    onNext,
    ErrorUtil::toast
)

fun <T> Single<T>.subscribeOrLog(onSuccess: (T) -> Unit) = this.subscribe(
    onSuccess,
    ErrorUtil::log
)

fun <T> Single<T>.subscribeOrToast(onSuccess: (T) -> Unit) = this.subscribe(
    onSuccess,
    ErrorUtil::toast
)
