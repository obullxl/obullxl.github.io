+++
slug = "2023092301"
date = "2023-09-23"
lastmod = "2023-09-23"
title = "Flutter/Dart第01天：Dart安装和初体验"
description = "编写一个App就能编译发布到iOS、Android和Web等各大平台的跨平台技术，各大厂商一直都有研究和发布对应技术产品，目前最热门的莫过于Flutter框架了。而Dart作为其唯一的编程语言，今天我们开始来体验一下……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台" ]
categories = [ "专业技术" ]
+++

Dart的安装方式有几种：一种是下载源代码，然后编译安装；一种是通过包管理工具进行安装。

Dart官方网站分表列出了针对Windows、Linux和MacOS的安装方式：[https://dart.dev/get-dart](https://dart.dev/get-dart)

我下面在个人MacOS上介绍`brew`包管理工具安装方法和过程：

## 安装HomeBrew包管理工具：brew
HomeBrew是MacOS的一个包管理工具，有了它，后面安装、更新、配置等操作就容易多了：[https://brew.sh](https://brew.sh)

## 获取Dart：brew tap dart-lang/dart
本命令作用是获取Dart的安装包列表。由于需要从GitHub拉取包数据，访问GitHub不稳定容易失败，重试几次即可：

```shell
SZH-MacBook:~ shizihu$ brew tap dart-lang/dart
Running `brew update --auto-update`...
==> Downloading https://ghcr.io/v2/homebrew/portable-ruby/portable-ruby/blobs/sha256:61029cec31c68a1fae1fa90fa876adf43d0becff777da793f9b5c5577f00567a
################################################################################### 100.0%
==> Pouring portable-ruby-2.6.10_1.el_capitan.bottle.tar.gz
==> Homebrew collects anonymous analytics.
Read the analytics documentation (and how to opt-out) here:
  https://docs.brew.sh/Analytics
No analytics have been recorded yet (nor will be during this `brew` run).

Installing from the API is now the default behaviour!
You can save space and time by running:
  brew untap homebrew/core
==> Downloading https://formulae.brew.sh/api/formula.jws.json
################################################################################### 100.0%
==> Downloading https://formulae.brew.sh/api/cask.jws.json
################################################################################### 100.0%
==> Tapping dart-lang/dart
Cloning into '/usr/local/Homebrew/Library/Taps/dart-lang/homebrew-dart'...
remote: Enumerating objects: 3737, done.
remote: Counting objects: 100% (1491/1491), done.
remote: Compressing objects: 100% (387/387), done.
remote: Total 3737 (delta 1242), reused 1301 (delta 1098), pack-reused 2246
Receiving objects: 100% (3737/3737), 674.85 KiB | 127.00 KiB/s, done.
Resolving deltas: 100% (2518/2518), done.
Tapped 22 formulae (53 files, 862.2KB).
```

## 安装Dart：brew install dart
从安装日志可以看出：
 - Dart安装版本：3.1.2
 - Dart安装路径：/usr/local/Cellar/dart/3.1.2
 - Dart命令路径：/usr/local/opt/dart/libexec

```shell
SZH-MacBook:~ shizihu$ brew install dart
==> Fetching dart-lang/dart/dart
==> Downloading https://storage.googleapis.com/dart-archive/channels/stable/release/3.1.2/
################################################################################### 100.0%
==> Installing dart from dart-lang/dart
Warning: A newer Command Line Tools release is available.
Update them from Software Update in System Preferences.

If that doesn't show you any updates, run:
  sudo rm -rf /Library/Developer/CommandLineTools
  sudo xcode-select --install

Alternatively, manually download them from:
  https://developer.apple.com/download/all/.
You should download the Command Line Tools for Xcode 14.2.

==> Caveats
Please note the path to the Dart SDK:
  /usr/local/opt/dart/libexec
==> Summary
🍺  /usr/local/Cellar/dart/3.1.2: 987 files, 541.6MB, built in 22 seconds
==> Running `brew cleanup dart`...
Disable this behaviour by setting HOMEBREW_NO_INSTALL_CLEANUP.
Hide these hints with HOMEBREW_NO_ENV_HINTS (see `man brew`).
==> `brew cleanup` has not been run in the last 30 days, running now...
Disable this behaviour by setting HOMEBREW_NO_INSTALL_CLEANUP.
Hide these hints with HOMEBREW_NO_ENV_HINTS (see `man brew`).
Removing: /Users/shizihu/Library/Caches/Homebrew/gettext--0.21... (8.7MB)
Removing: /Users/shizihu/Library/Caches/Homebrew/git--2.37.3... (16.1MB)
Removing: /Users/shizihu/Library/Caches/Homebrew/pcre2--10.40... (2MB)
Removing: /Users/shizihu/Library/Caches/Homebrew/git_bottle_manifest--2.37.3... (11.4KB)
Removing: /Users/shizihu/Library/Caches/Homebrew/gettext_bottle_manifest--0.21... (10.5KB)
Removing: /Users/shizihu/Library/Caches/Homebrew/pcre2_bottle_manifest--10.40... (7.5KB)
Removing: /Users/shizihu/Library/Logs/Homebrew/icu4c... (64B)
Removing: /Users/shizihu/Library/Logs/Homebrew/gettext... (64B)
Removing: /Users/shizihu/Library/Logs/Homebrew/pcre2... (64B)
Removing: /Users/shizihu/Library/Logs/Homebrew/node... (64B)
Removing: /Users/shizihu/Library/Logs/Homebrew/git... (64B)
```

## 检测Dart：brew info dart
下面检测Dart安装情况：

```shell
SZH-MacBook:~ shizihu$ brew info dart
==> dart-lang/dart/dart: stable 3.1.2, HEAD
SDK
https://dart.dev
Conflicts with:
  dart-beta (because dart-beta ships the same binaries)
/usr/local/Cellar/dart/3.1.2 (987 files, 541.6MB) *
  Built from source on 2023-09-22 at 13:10:51
From: https://github.com/dart-lang/homebrew-dart/blob/HEAD/Formula/dart.rb
==> Options
--HEAD
	Install HEAD version
==> Caveats
Please note the path to the Dart SDK:
  /usr/local/opt/dart/libexec
```

## 升级Dart：brew upgrade dart
可选操作，因为是刚安装好，肯定是最新版本：

```shell
SZH-MacBook:~ shizihu$ brew upgrade dart
Warning: No remote 'origin' in /usr/local/Homebrew/Library/Taps/homebrew/homebrew-services, skipping update!
Warning: dart-lang/dart/dart 3.1.2 already installed
```

## 第一个Dart程序：HelloWorld.dart
编写Dart程序：00-HelloWorld.dart

```dart
void main() {
  print('Hello, World!');

  final list = [];
  list.add(1);
  list.add('2');
  list.add('a');

  printList(list);
}

void printList(var alist) => print(alist);
```

## 执行Dart程序：dart run 00-HelloWorld.dart
```shell
SZH-MacBook:ntopic-dart shizihu$ dart run 00-HelloWorld.dart 
Hello, World!
[1, 2, a]
```

## 最后
初试Dart，感觉和Java有点类似：main入口函数，print输出，列表对象和方法。

接下来的博客中，我会专门通过Dart和Flutter编写一款应用程序，并发布到不同的应用市场！

---
我的本博客原地址：[https://ntopic.cn/p/2023092301/](https://ntopic.cn/p/2023092301)

---
