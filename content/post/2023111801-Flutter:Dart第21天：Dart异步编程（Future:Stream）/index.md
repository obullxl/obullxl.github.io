+++
slug = "2023111801"
date = "2023-11-18"
lastmod = "2023-11-18"
title = "Flutter/Dart第21天：Dart异步编程（Future/Stream）"
description = "Dart库中有大量返回结果为Future或Stream类型的函数，它们都是异步函数，函数的返回结果在构建可能存在耗时操作之后就返回了（如：网络IO操作），而不是同步等到这些耗时操作完成后在返回。关键字async和wait简化了异步编程（如：回调地狱），让异步代码的编写看起来像同步代码一样……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "异步", "Future", "async-await", "Stream" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/async](https://dart.dev/language/async)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## Future处理
我们有2种方式编写Future异步代码：
 - 使用`async`和`wait`关键字
 - 使用Future API（[https://dart.dev/guides/libraries/library-tour#future](https://dart.dev/guides/libraries/library-tour#future)）

推荐使用`async`和`wait`关键字，让异步代码看起来和同步代码一样。

如下代码样例：`wait`关键字等待异步函数返回结果，它必须在`async`函数中。

```dart
Future<void> checkVersion() async {
  var version = await lookUpVersion();
  var exitCode = await findExitCode();
  // ......
  await flushThenExit(exitCode);
}
```

可以用`try`、`catch`和`finally`关键字处理错误和清理代码：

```dart
try {
  version = await lookUpVersion();
} catch (e) {
  // React to inability to look up the version
} finally {
  // Clean code
}
```

## 申明异步函数
使用`async`修饰一个函数体，这个函数就是异步函数，它的返回结果是一个`Future<T>`，当函数无需返回结果时，返回结果为`Future<void>`：

```dart
Future<String> lookUpVersion async {
  // ......
  return '1.0.1';
}

Future<void> doSomething() {
  // ......
}
```

## Stream处理
同样的，我们也有2种方式编写Stream代码：
 - 在异步循环使用`async`异步关键字（`await for`）
 - 使用Stream API（[https://dart.dev/guides/libraries/library-tour#stream](https://dart.dev/guides/libraries/library-tour#stream)）

推荐使用`async`关键字，异步循环的形式如下：

```dart
await for (varOrType identifier in expression) {
  // ......
}
```

在上诉异步循环形式中，`expression`的值为一个Stream类型，异步循环的执行过程如下：
 - 等待Stream产出一个值（即触发循环执行）
 - 设置<b>identifier</b>变量值，执行循环体逻辑
 - 重复上面2步，直到Stream关闭（即循环结束）

我们可以通过`break`或者`return`语句，退出循环，从而中断监听Stream生产的值。

同样的，异步循环的函数，必须使用`async`关键字修饰。如下所示，`main()`函数体使用了`await for`异步循环，则函数必须使用`async`修饰：

```dart
void main() async {
  // ......
  await for (final request in requestServer) {
    handleRequest(request);
  }
  // ......
}
```

## 最后
- 第4课对异步编程文档（<b>Future和async/await</b>）：[https://ntopic.cn/p/2023092901](https://ntopic.cn/p/2023092901)
- 其他更多关于异步编程信息，请求参考异步包`dart:async`（[https://dart.dev/guides/libraries/library-tour#dartasync---asynchronous-programming](https://dart.dev/guides/libraries/library-tour#dartasync---asynchronous-programming)）

---
我的本博客原地址：[https://ntopic.cn/p/2023111801](https://ntopic.cn/p/2023111801/)

---
