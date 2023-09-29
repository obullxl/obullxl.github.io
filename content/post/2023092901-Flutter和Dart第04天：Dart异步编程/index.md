+++
slug = "2023092901"
date = "2023-09-29"
lastmod = "2023-09-29"
title = "Flutter/Dart第04天：Dart异步编程（Future和async/await）"
description = "在前面几天中，我们学习了Dart基础语法、可迭代集合，它们是Flutter应用研发的基本功。今天，我们继续学习Flutter应用另一个必须掌握知识点：异步编程（即Future和async/await）。它类似于Java中的FutureTask、JavaScript中的Promise。它是后续Flutter应用研发中有关API调用、文件处理、DB数据库操作等异步操作的基础……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "异步", "Future", "async-await" ]
categories = [ "专业技术" ]
+++

Dart官网代码实验室：[https://dart.dev/codelabs/async-await](https://dart.dev/codelabs/async-await)

<b>重要说明：</b>本博客基于Dart官网代码实验室，但并不是简单的对官网文章进行翻译，我会根据个人研发经验，在覆盖官网文章核心内容情况下，加入自己的一些扩展问题和问题演示和总结，包括名称解释、使用场景说明、代码样例覆盖、最后完整的场景编程等。

## 启蒙：错误的异步编程样例
下面是一个错误的异步编程样例，大概过程：通过模拟网络API获取订单ID，然后组织订单ID文案，最终输出问题。

我们期望最终输出的是正确的订单ID文案，可结果并不符合我们的期望：订单ID并不是<b>T2023092900001</b>，而是<b>Instance of 'Future\<String\>'</b>

```dart
// 1.1 创建订单消息
String createOrderMessage() {
  var order = fetchOrderID();
  return '订单ID: $order';
}

// 1.2 获取订单ID内容
Future<String> fetchOrderID() =>
  // 假设获取订单ID是一次网络交互，处理过程需要2秒钟，因此模拟了2秒钟返回订单ID
  Future.delayed(
  const Duration(seconds: 2),
  () => 'T2023092900001',
);

void main() {
  // 1. 启蒙：错误的异步编程样例
  final message = createOrderMessage();
  print(message);

  // 结果：订单ID: Instance of 'Future<String>'
}
```

同步编程和异步编程说明：
 - <b>同步编程：</b>按照代码块顺序执行代码块，前面代码块没有执行完成之前，后面代码被<b>阻塞</b>。
 - <b>异步编程：</b>异步操作代码块完成<b>初始化</b>之后，后面代码块就可以执行了（非阻塞），异步代码块执行完成（如上面样例等待2秒钟），执行完成回调代码块（即<b>回调</b>）。

## Future异步结果说明
异步操作的结果都是`Future`类的实例（[https://api.dart.cn/stable/3.1.3/dart-async/Future-class.html](https://api.dart.cn/stable/3.1.3/dart-async/Future-class.html)），异步操作有2种状态：<b>未完成</b>和<b>完成</b>状态。调用异步代码块（或函数），返回值都是未完成状态的结果。
 - 未完成状态：调用一个异步函数，返回一个`Future<T>`结果，在异步操作<b>执行结束</b>或者<b>执行出错</b>之前的状态。
 - 完成状态：异步操作执行结束正常返回结果或者执行出错，都是完成状态。正常完成的返回结果即`Futrue<T>`的T（如：`Future<String>`），如果无需返回结果，则为void，即异步函数的返回值为`Future<void>`。如果异步函数执行出错，则返回结果是一个`Error`，可以进行捕获。

下面2个代码样例，分别为返回值为void和出错结果：

```dart
// 2.1 异步操作无返回值
Future<void> fetchOrderID2() {
  // 模拟了2秒钟输出了订单ID
  return Future.delayed(const Duration(seconds: 2), () => print('ID2:T2023092900002'));
}

// 2.2 异常操作返回错误
Future<void> fetchOrderID3() {
  return Future.delayed(const Duration(seconds: 2), () => throw Exception('网络异常'));
}

void main() {
  // 2. Future/async/await异步结果说明
  fetchOrderID2();
  print('2. fetchOrderID2-Future/async/await异步结果说明...');

  // 结果：
  // 2. fetchOrderID2-Future/async/await异步结果说明...
  // ID2:T2023092900002
  
  fetchOrderID3();
  print('2. fetchOrderID3-Future/async/await异步结果说明...');

  // 结果：
  // 2. fetchOrderID3-Future/async/await异步结果说明...
  // Unhandled exception:
  // Exception: 网络异常
}
```

## async/await异步操作定义和使用
<b>async</b>定义一个异步操作，而<b>await</b>则是使用一个异步操作的结果。

在应用async和await是，有2点需要遵守的<b>基本规则</b>：
1. 如果需要定义一个异步函数，则在函数体之前增加<b>async</b>关键字
2. 只有在异步函数中（即函数体前有async关键字的函数），<b>await</b>关键字才会生效（也就是await必须配合async使用）

定义一个异步函数方法样例（即增加async关键字）：

```dart
// 1. 同步函数转异步函数：无返回结果
// 1.1 同步函数
void funcVoid() {}

// 1.2 异步返回
Future<void> funcVoid() async {}

// 2. 同步函数转异步函数：有返回结果
// 2.1 同步函数
String funcResult() {}

// 2.2 异常函数
Future<String> funcResult() async {}
```

接下来，我们来重写第1张中，异步函数代码，以使结果符合我们预期：
1. 获取订单ID函数`fetchOrderID()`前，增加`await`关键字。
2. 创建订单消息的函数`createOrderMessageV2()`，增加`async`关键字变成异步函数，同返回结果由`String`变成`Future<String>`异步结果。
3. 同样的，`main()`函数调用了异步函数，因此也需要增加`async`关键字。

```dart
// 3. async/await异步操作定义和使用
Future<String> createOrderMessageV2() async {
  var order = await fetchOrderID();
  return '订单ID: $order';
}

void main() async {
  // 3. async/await异步操作定义和使用
  final messageV2 = await createOrderMessageV2();
  print('3. async/await异步操作定义和使用');
  print('$messageV2');

  // 结果：
  // 3. async/await异步操作定义和使用
  // 订单ID: T2023092900001
}
```

## try/catch异步操作的异常处理
在第2章节中，`fetchOrderID3()`异步方法会抛出异常，从而中断程序处理。异步操作的异常，我们也可以和同步函数调用一样，通过<b>try-catch</b>的方式进行处理。

下面我们把`fetchOrderID3()`使用的地方进行改写，捕获异常从而不中断我们的程序：

```dart
void main() async {
  // 4. try/catch异步操作的异常处理
  try {
    await fetchOrderID3();
    print('4. try/catch异步操作的异常处理.');
  } catch (e) {
    print('4. try/catch异步操作的异常处理: $e');
  }

  // 结果：4. try/catch异步操作的异常处理: Exception: 网络异常
}
```

## 场景编程：异步编程的大合唱
应用场景假设：对当前登录的用户打个招呼，同时用户退出登录。因为退出登录操作可被降级，因此退出登录需要捕获所有异常。
1. 获取当前登录的用户名，因为是网络API操作，因此是异步操作。
2. 退出登录时，需要获取当前缓存的用户名，设计到存储操作，因此也是异步操作。

```dart
// 5.1 组装用户欢迎语
String makeGreeting(String userName) {
  return '欢迎你 $userName';
}

// 5.2 获取用户名，异步操作
Future<String> fetchUserName() async {
  return Future.delayed(const Duration(seconds: 2), () => 'NTopic.CN');
}

// 5.3 用户登录-打声招呼
Future<String> greeting() async {
  final userName = await fetchUserName();
  return makeGreeting(userName);
}

// 5.4 用户退出-再见
Future<String> goodbye() async {
  final userName = await fetchUserName();
  return '$userName 下次再见！';
}

void main() async {
  // 5. 场景编程：异步编程的大合唱
  print('5. 场景编程：异步编程的大合唱...');
  print(await greeting());

  try {
    print(await goodbye());
  } catch (e) {
    print('5. 场景编程：异步编程的大合唱-Goodbye异常: $e');
  }

  // 结果：
  // 5. 场景编程：异步编程的大合唱...
  // 欢迎你 NTopic.CN
  // NTopic.CN 下次再见！
}
```

## 最后-完整的实例代码
本文介绍的完整的实例代码：

```dart
// 第04天：异步编程

// 1.1 创建订单消息
String createOrderMessage() {
  var order = fetchOrderID();
  return '订单ID: $order';
}

// 1.2 获取订单ID内容
Future<String> fetchOrderID() =>
    // 假设获取订单ID是一次网络交互，处理过程需要2秒钟，因此模拟了2秒钟返回订单ID
    Future.delayed(
      const Duration(seconds: 2),
      () => 'T2023092900001',
    );

// 2.1 异步操作无返回值
Future<void> fetchOrderID2() {
  // 模拟了2秒钟输出了订单ID
  return Future.delayed(const Duration(seconds: 2), () => print('ID2:T2023092900002'));
}

// 2.2 异常操作返回错误
Future<void> fetchOrderID3() {
  return Future.delayed(const Duration(seconds: 2), () => throw Exception('网络异常'));
}

// 3. async/await异步操作定义和使用
Future<String> createOrderMessageV2() async {
  var order = await fetchOrderID();
  return '订单ID: $order';
}

// 5.1 组装用户欢迎语
String makeGreeting(String userName) {
  return '欢迎你 $userName';
}

// 5.2 获取用户名，异步操作
Future<String> fetchUserName() async {
  return Future.delayed(const Duration(seconds: 2), () => 'NTopic.CN');
}

// 5.3 用户登录-打声招呼
Future<String> greeting() async {
  final userName = await fetchUserName();
  return makeGreeting(userName);
}

// 5.4 用户退出-再见
Future<String> goodbye() async {
  final userName = await fetchUserName();
  return '$userName 下次再见！';
}

void main() async {
  // 1. 启蒙：错误的异步编程样例
  final message = createOrderMessage();
  print(message);

  // 2. Future/async/await异步结果说明
  fetchOrderID2();
  print('2. fetchOrderID2-Future/async/await异步结果说明...');

  // fetchOrderID3();
  print('2. fetchOrderID3-Future/async/await异步结果说明...');

  // 3. async/await异步操作定义和使用
  final messageV2 = await createOrderMessageV2();
  print('3. async/await异步操作定义和使用');
  print('$messageV2');

  // 4. try/catch异步操作的异常处理
  try {
    await fetchOrderID3();
    print('4. try/catch异步操作的异常处理.');
  } catch (e) {
    print('4. try/catch异步操作的异常处理: $e');
  }

  // 5. 场景编程：异步编程的大合唱
  print('5. 场景编程：异步编程的大合唱...');
  print(await greeting());

  try {
    print(await goodbye());
  } catch (e) {
    print('5. 场景编程：异步编程的大合唱-Goodbye异常: $e');
  }
}
```

---
我的本博客原地址：[https://ntopic.cn/p/2023092901](https://ntopic.cn/p/2023092901/)

---
