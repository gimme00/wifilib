# wifilib

[![GitHub release](https://img.shields.io/github/release/selpic-handy-printer/wifilib.svg)](https://github.com/selpic-handy-printer/wifilib/releases/latest)
[![sample](https://img.shields.io/badge/fir.im-sample-brightgreen)](https://fir.im/tmqu)
[![jitpack](https://jitpack.io/v/selpic-handy-printer/wifilib.svg)](https://jitpack.io/#selpic-handy-printer/wifilib)
[![license|MIT](https://img.shields.io/github/license/selpic-handy-printer/wifilib)](https://github.com/selpic-handy-printer/wifilib/blob/master/LICENSE)

English|[Chinese](./README.zh.md)

## Add a dependency

```groovy
implementation('com.selpic.sdk:wifilib:1.1.1@aar') { transitive = true }
```

Add contents for `build.gradle` in project root directory as follows:

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

## Usages

Create global instance

```kotlin
val printer = SelpicPrinterFactory.create(appContext)
```

Get device information

```kotlin
printer.getDeviceInfo()
    .subscribe(
        { /*onSuccess*/ },
        { /*onError*/ }
    )
```

Set up print parameters

```kotlin
printer.setPrintParam(PrintParam(12, 1, 0))
    .subscribe(
        { /*onSuccess*/ },
        { /*onError*/ }
    )
```

Send print data

```kotlin
printer.sendPrintData(bitmap)
       .subscribe(
           { /*onNext*/  },
           { /*onError*/ }
       )
```

## API documentation

[API reference](https://jitpack.io/com/github/selpic-handy-printer/wifilib/master-SNAPSHOT/javadoc/)
