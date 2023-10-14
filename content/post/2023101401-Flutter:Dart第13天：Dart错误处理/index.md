+++
slug = "2023101401"
date = "2023-10-14"
lastmod = "2023-10-14"
title = "Flutter/Dart第13天：Dart错误处理"
description = "错误也可以理解为异常，代表应用程序在执行过程中的发生了非预期的行为，常见异常比如有空指针、数组越界、网络超时、IO异常等，Dart语言也支持抛出和捕获异常。和Java不同的是，Dart语言只有未检测异常。Dart中的异常需要被捕获并被处理，否则可能导致程序退出……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "错误" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/error-handling](https://dart.dev/language/error-handling)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## 异常
和Java一样，Dart也可以抛出异常，也可以捕获异常。Dart异常如果未被捕获，容易导致进程挂起和导致程序退出。所以，我们在编写Dart程序时，需要特别关注异常。

和Java不同的是，Dart中的异常全部都是<b>未检测异常</b>。如果一个方法没有申明任何类型的异常，那么我们就<b>无需捕获</b>该方法的异常。

Dart提供了`Exception`和`Error`两种异常的基础类型，同时内置了其他一些它们的子类。我们也可以定义我们自己的异常类型，Dart可以把<b>任何非空的对象</b>当作异常抛出，且这些对象类型不一定是`Exception`或`Error`类型的子类（和Java有很大区别）。

### throw抛出异常
Dart可以把任务非空对象当作异常抛出，但<b>建议</b>抛出的异常为`Exception`或者`Error`的子类，下面是几个代码样例：

```dart
throw FormatException('Expected at least 1 section');

throw 'Out of llamas!';

void distanceTo(Point other) => throw UnimplementedError();
```

### catch捕获异常
捕获异常，一般有种目的：一是为了阻止异常继续传播，二是我们期望重新抛出异常。

捕获异常的语法有几种：
 - on 异常类型 catch(异常对象)
 - catch(异常对象）
 - catch(异常对象, 异常堆栈)

捕获异常后，我们可以进行一些处理。我们可以根据<b>异常类型</b>，捕获多个异常。异常语句中若未定义异常类型，则捕获所有异常，如下代码样例：

```dart
try {
  breedMoreLlamas();
} on OutOfLlamasException {
  // `OutOfLlamasException`类型的异常
  buyMoreLlamas();
} on Exception catch (e) {
  // `Exception`类型的异常
  print('Unknown exception: $e');
} catch (e) {
  // 其他的所有的异常
  print('Something really unknown: $e');
}
```

异常堆栈类型`StackTrace`类型，如下代码样例：

```dart
try {
  // ···
} on Exception catch (e) {
  print('Exception details:\n $e');
} catch (e, s) {
  print('Exception details:\n $e');
  print('Stack trace:\n $s');
}
```

### rethrow重新抛出异常
如下代码样例，当捕获到异常之后，可以通过`rethrow`重新抛出异常：

```dart
void misbehave() {
  try {
    dynamic foo = true;
    print(foo++); // 制造异常
  } catch (e) {
    print('misbehave() partially handled ${e.runtimeType}.');
    rethrow; // 重新抛出异常
  }
}

void main() {
  try {
    misbehave();
  } catch (e) {
    print('main() finished handling ${e.runtimeType}.');
  }
}
```

### finally子句
当我们不论是否有异常发生，都行执行某个逻辑时，我们可用使用`finally`子句。当发生异常时，首先执行`catch`子句进行捕获，若未被捕获或者重新抛出异常，那么这个异常会在执行完`fianlly`之后被抛出。

```dart
try {
  breedMoreLlamas();
} finally {
  // 不论是否有异常发生，均会执行本逻辑
  cleanLlamaStalls();
}

try {
  breedMoreLlamas();
} catch (e) {
  // 首先：捕获异常
  print('Error: $e');
} finally {
  // 然后：执行本逻辑
  cleanLlamaStalls();
}
```

## 断言
在应用的<b>开发阶段</b>，通过结果为`false`的断言语句（如：`assert(<condition>, <optionalMessage>);`语句）来阻断程序执行。几种断言语句的代码样例：

```dart
// `text`不能为null
assert(text != null);

// `number`必须小于100
assert(number < 100);

// `urlString`必须以`https`开头
assert(urlString.startsWith('https'), 'URL ($urlString) should start with "https".');
```

断言失败（表达式结果为`false`）时，抛出`AssertionError`类型（`Error`类型的子类）的异常。

断言在以下场景下会生效：
 - Flutter应用程序在<b>debug模式</b>（开发阶段，开启了断言）。
 - 类似于<b>webdev</b>等开发工具，默认开启了断言。
 - 类似于`dart run`和`dart compile js`等工具，且通过命令行增加了`--enable-asserts`开启断言参数。

而在<b>生产阶段</b>，断言相关代码被忽略，因此断言会失效（不会执行）。

---
我的本博客原地址：[https://ntopic.cn/p/2023101401](https://ntopic.cn/p/2023101401/)

---
