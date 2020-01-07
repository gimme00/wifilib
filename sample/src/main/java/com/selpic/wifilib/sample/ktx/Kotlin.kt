package com.selpic.wifilib.sample.ktx


@Deprecated("In some cases will cause Jetpack Compose compilation error ...")
inline fun pass() {

}
@Deprecated("@see pass()")
inline fun <T> pass(t: T) {

}

fun debugger(){
    println("debugger")
}