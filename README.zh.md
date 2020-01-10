# wifilib

[![GitHub release](https://img.shields.io/github/release/selpic-handy-printer/wifilib.svg)](https://github.com/selpic-handy-printer/wifilib/releases/latest)
[![jitpack](https://jitpack.io/v/selpic-handy-printer/wifilib.svg)](https://jitpack.io/#selpic-handy-printer/wifilib)
[![license|MIT](https://img.shields.io/github/license/selpic-handy-printer/wifilib)](https://github.com/selpic-handy-printer/wifilib/blob/master/LICENSE)

[English](./README.md)|Chinese

## 添加依赖

```groovy
implementation('com.selpic.sdk:wifilib:1.1.0@aar') { transitive = true }
```

项目根目录下的`build.gradle`需要添加如下内容：

```groovy
allprojects {
    repositories {
        // https://github.com/TranscodeGroup/lib-module
        maven { url "https://jitpack.io" }
        // https://help.github.com/en/github/managing-packages-with-github-packages/configuring-gradle-for-use-with-github-packages
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/selpic-handy-printer/wifilib")
            credentials {
                // replace to yourself github account
                username = "github-packages-public-token"
                password = "40c403-bff7f614-9bd0a6-4fb87-0b2e2e45a672fda".replaceAll("-", "")
            }
        }
    }
}
```

## 使用

创建全局单例

```kotlin
val printer = SelpicPrinterFactory.create(appContext)
```

获取设备信息

```kotlin
printer.getDeviceInfo()
    .subscribe(
        { /*onSuccess*/ },
        { /*onError*/ }
    )
```

设置打印参数

```kotlin
printer.setPrintParam(PrintParam(12, 1, 0))
    .subscribe(
        { /*onSuccess*/ },
        { /*onError*/ }
    )
```

发送打印数据

```kotlin
printer.sendPrintData(bitmap)
       .subscribe(
           { /*onNext*/  },
           { /*onError*/ }
       )
```

## API文档

[API reference](https://jitpack.io/com/github/selpic-handy-printer/wifilib/1.1.0/javadoc/)
