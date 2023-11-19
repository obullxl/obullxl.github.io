+++
slug = "2023112001"
date = "2023-11-19"
lastmod = "2023-11-19"
title = "FlutterApp实战·第01天：Flutter安装和配置"
description = "前面多文介绍了Dart编程语言的基本语法和语言特性。从本文开始，我们通过一个Flutter App的编码过程，完成Flutter的学习，包括Flutter基础知识，Flutter App启动页，Tab页，个人设置页，SQLite数据库，HTTP API调用，到最后Flutter App打包等……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台" ]
categories = [ "专业技术" ]
+++

Flutter安装文档：
 - 官方文档：[https://docs.flutter.dev/get-started/install](https://docs.flutter.dev/get-started/install)
 - 中文文档：[https://flutter.cn/docs/get-started/install](https://flutter.cn/docs/get-started/install)

## Dart升级
 - Dart安装参考前面文章（Dart安装和初体验）：[https://ntopic.cn/p/2023092301](https://ntopic.cn/p/2023092301)
 - 后续学习我们采用最新Flutter版本，因此建议升级Dart最新版本（当前：<b>3.2.0</b>）：`brew upgrade dart`

```shell
$ brew info dart
==> dart-lang/dart/dart: stable 3.2.0, HEAD
SDK
https://dart.dev
Conflicts with:
  dart-beta (because dart-beta ships the same binaries)
/usr/local/Cellar/dart/3.2.0 (1,022 files, 560.8MB) *
  Built from source on 2023-11-19 at 10:04:05
From: https://github.com/dart-lang/homebrew-dart/blob/HEAD/Formula/dart.rb
==> Options
--HEAD
  Install HEAD version
==> Caveats
Please note the path to the Dart SDK:
  /usr/local/opt/dart/libexec
```

## VS Code安装
VS Code是免费的，支持Flutter的研发、调试和运行，没有理由不使用它作为Flutter的研发IDE（IntelliJ IDEA非常强大，可惜是收费）。

下载和安装VS Code下载：
 - 首先通过官网下载VS Code：[https://code.visualstudio.com/docs/?dv=win](https://code.visualstudio.com/docs/?dv=win)
 - 点击下载之后，发现下载速度很慢：右键复制下载链接，然后把域名换成 “<b>vscode.cdn.azure.cn</b>”，重新通过浏览器打开进行下载，速度杠杠的

VS Code安装成功之后，就进行Flutter安装和配置。

## Flutter SDK安装
我们可以从GitHub原始仓库和Gitee镜像仓库下载，由于中国访问GitHub网速太慢，建议从<b>Gitee镜像仓库</b>下载：

```shell
# 源代码目录 [/Users/obullxl/FlutterSpace]
$ cd /Users/obullxl/FlutterSpace

# Gitee下载 [推荐方式]
$ git clone -b stable https://gitee.com/mirrors/Flutter.git flutter

# GitHub下载 [网速可能较慢]
$ git clone -b stable https://github.com/flutter/flutter.git flutter
```

Flutter SDK有多个版本，如`beta`、`master`、`stable`等，其中beta/master为开发分支，`stable`为稳定分支，建议使用<b>稳定分支</b>：
 - 查看所有分支：`flutter channel`
 - 切换本地分支：`flutter channel xxx`
 - 查看当前版本：`flutter --version`
 - 升级最新版本：`flutter upgrade`
 - 检测Flutter配置：`flutter doctor`

```shell
# 切换到Flutter源代码目录
$ cd /Users/obullxl/FlutterSpace/flutter

# 查看Flutter所有分支 [建议stable分支]
$ flutter channel
Flutter channels:
  master
  main
  beta
* stable

# 切换到stable分支
$ flutter channel stable

# 查看当前版本，由于我的Flutter是之前安装的版本[3.7.1]
$ flutter --version
Flutter 3.7.1 • channel stable • https://gitee.com/mirrors/Flutter.git
Framework • revision 7048ed95a5 (10 months ago) • 2023-02-01 09:07:31 -0800
Engine • revision 800594f1f4
Tools • Dart 2.19.1 • DevTools 2.20.1

# 升级Flutter版本
$ flutter upgrade

# 再次查看Flutter版本，已经是最终版本[3.16.0]
$ flutter --version
Flutter 3.16.0 • channel stable • https://gitee.com/mirrors/Flutter.git
Framework • revision db7ef5bf9f (3 days ago) • 2023-11-15 11:25:44 -0800
Engine • revision 74d16627b9
Tools • Dart 3.2.0 • DevTools 2.28.2

# 检测Flutter配置情况
$ flutter doctor
Doctor summary (to see all details, run flutter doctor -v):
[!] Flutter (Channel stable, 3.16.0, on macOS 12.6.7 21G651 darwin-x64, locale zh-Hans-CN)
    ! Warning: `dart` on your path resolves to
      /usr/local/Cellar/dart/3.1.2/libexec/bin/dart, which is not inside your current
      Flutter SDK checkout at /Users/obullxl/FlutterSpace/flutter. Consider adding
      /Users/obullxl/FlutterSpace/flutter/bin to the front of your path.
[!] Android toolchain - develop for Android devices (Android SDK version 33.0.1)
    ✗ cmdline-tools component is missing
      Run `path/to/sdkmanager --install "cmdline-tools;latest"`
      See https://developer.android.com/studio/command-line for more details.
    ✗ Android license status unknown.
      Run `flutter doctor --android-licenses` to accept the SDK licenses.
      See https://flutter.dev/docs/get-started/install/macos#android-setup for more
      details.
[✓] Xcode - develop for iOS and macOS (Xcode 14.2)
[✓] Chrome - develop for the web
[✓] Android Studio (version 2021.3)
[✓] IntelliJ IDEA Ultimate Edition (version 2021.1.3)
[✓] VS Code (version 1.84.0)
[✓] Connected device (2 available)
[✓] Network resources

! Doctor found issues in 2 categories.
```

## Flutter 仓库配置
和Java编程需要配置Maven国内仓库一样，Flutter也需要配置国内仓库，否则访问国外仓库要么访问不了，要么网速太慢。

在环境变量中，增加如下配置：

```shell
# 建软连接 [便于后面统一管理]
$ cd /opt
$ sudo ln -s /Users/obullxl/FlutterSpace/flutter ./flutter

# 系统变量
$ sudo vi /etc/profile

# 增加内容
export PUB_HOSTED_URL=https://pub.flutter-io.cn
export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn

export FLUTTER_GIT_URL=https://gitee.com/mirrors/Flutter.git

export PATH=$PATH:/opt/flutter/bin

# 生效环境变量
$ source /etc/profile
```

## 最后
至此，Flutter安装和配置已经完成了，接下来我们就通过VS Code编写Flutter App了！

---
我的本博客原地址：[https://ntopic.cn/p/2023112001](https://ntopic.cn/p/2023112001/)

---
