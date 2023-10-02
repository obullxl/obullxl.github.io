+++
slug = "2023100201"
date = "2023-10-02"
lastmod = "2023-10-02"
title = "Flutter/Dart第07天：Dart基础语法详解（库、导入和关键字）"
description = "我们前面完成了Dart语言基础特性的学习，包括基础语法概览、迭代集合、异步编程、Mixin高级特性和变量等。今天我们来学习Dart的库相关知识，包括如何导入库、指定库前缀、导入部分或者排除部分库、延迟导入库等，最后看下Dart中67个关键字作为标识符的一些约束……"
image = "00.jpg"
tags = [ "Dart", "Flutter" ]
categories = [ "专业技术" ]
+++

Dart官网文档：[https://dart.dev/language/libraries](https://dart.dev/language/libraries)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## Dart中的库（Library）
Dart语言对代码的复用下了不少功夫，如前面讲到的`Mixin`高级特性实现类级别代码复用。本文介绍另一种更宽广的代码复用：<b>库</b>。

Dart库可以认为是一组复用类、功能等的集合，它则代表了一个共享的代码模块，任何一个`.dart`文件都是一个库。

Dart库有一个隐含的规则：凡是以下划线`_`开头的标识符仅在本库内部可见，其他在库内外部均可见。（Dart没有private/protected/public可见域标识符，那么对应Java语言，下划线`_`开头为`private`，其他的为`public`，Dart没有protected）。

Dart库可以基本可分为3种，建议在使用时严格按照这3类导入库：
1. Dart中的内置库，即SDK中的库（对于Java语言如java.util.Map），以`dart:`作为命名空间。
2. Dart包中心仓库的库（对于Java语言为Maven中心仓库），以`package:`作为命名空间。
3. 本地文件代码库，以相对`路径`或者绝对路径作为命名空间。当然，本地代码块也可以使用`package`作为命名空间，容易和第2中混淆，因此不推荐。

## 如何使用库（import关键字）
通过`import`关键字指定在一个库中使用另外一个命名空间的库。

<b>代码样例：</b>如下代码，我们导入了Dart内置的`html`库、中心仓库`get`库，和`本地`库。

```dart
// 内置库
import 'dart:html';

// 中心仓库
import 'package:get/get.dart';

// 本地代码库
import '../01-ntopic-hellodart.dart';
```

## 指定库前缀（as关键字）
研发过程中，我们有时会导入有相同标识符，那么在使用过程中就会产生冲突，这时可以通过`as`关键字进行重命名库，同时在使用的时候，增加前缀进行区分。

<b>代码样例：</b>如下代码，我们导入了<b>lib1</b>和<b>lib2</b>两个库，它们都有<b>Element</b>这个类。

```dart
import 'package:lib1/lib1.dart';
import 'package:lib2/lib2.dart' as lib2;

// `lib1`库中Element类
Element element1 = Element();

// `lib2`库中Element类，通过`lib2`前缀解决冲突
lib2.Element element2 = lib2.Element();
```

## 导入部分库（show/hide关键字）
我们有时候只需要用到某个库的一部分代码或者类，或者需要排除某个库中指定的代码，通过增加`show`和`hide`关键字可实现。

<b>代码样例：</b>如下代码，我们用到了<b>lib1</b>和<b>lib2</b>两个库，<b>lib1</b>通过`show`仅导入了<b>foo</b>这个标识符（类名、常量名等），而<b>lib2</b>通过`hide`导入除<b>foo</b>之外的其他所有标识符。

```dart
// 仅导入`foo`
import 'package:lib1/lib1.dart' show foo;

// 导入除`foo`之外所有
import 'package:lib2/lib2.dart' hide foo;
```

## 延迟导入库（deferred as关键字）
Web应用中通过延迟导入库，可以实现按需加载，如下几种常见的应用场景：
1. 减少Web应用的启动时间。
2. A/B测试，比如切流不同的算法库。
3. 不常用的功能，比如可选页面、弹窗等。

<b>代码样例：</b>如下代码，通过`deferred as`关键字，实现延迟导入库，在实际使用库功能时需要导入库(如：`await hello.loadLibrary();`），在库加载完成之前，会阻塞代码执行。执行`loadLibrary()`方法仅实际加载库一次，后续调用该方法不会重复加载。

```dart
// 1. 延迟导入库
import 'package:greetings/hello.dart' deferred as hello;

// 2. 实际使用时，使用库前缀
Future<void> greet() async {
  // 加载库，加载成功之前阻塞后续代码执行，多次调用仅加载一次
  await hello.loadLibrary();
  hello.printGreeting();
}
```

<b>特别注意：</b>
1. 延迟导入库中的常量，在该库导入之前并不存在，因此它们并不是<b>导入库</b>（即使用它们的文件）中的常量。
2. 延迟导入库在导入之前并不存在，因此<b>导入库</b>不能使用它们的类型（包括类、枚举等），可以考虑把公共代码（如基础类、接口等）抽取出来作为单独的库，让延迟导入库和导入库共同导入使用。
3. 当使用`deferred as XXX`延迟导入库时，Dart隐含的给`loadLibrary()`增加了`XXX`命名空间。如上代码，`loadLibrary()`方法是异步的，返回一个`Future`。

## Dart中的关键字（共67个）
Dart官方文档：[https://dart.dev/language/keywords](https://dart.dev/language/keywords)

Dart关键字一共包含67个，本博客不重复列出这67个关键字，仅对关键字进行说明：
1. <b>尽量避免</b>使用这些关键字作为标识符，如有必要，表格中有`1`，`2`，`3`上标的关键字在部分常见可以使用作为标识符。
2. 上标为`1`的关键字与<b>上下文</b>有关，离开了上下文，这些关键字可以作为标识符。如`show`和`hide`关键在只在导入库上下文文中用到，其他地方可以正常作为标识符。
3. 上标为`2`的关键字是Dart<b>内置</b>的标识符，它们在大部分场景可以使用，但是禁止在类名、类型名和指定库的前缀（其实也是类型的一种）。
4. 上标为`3`的关键字（共2个：`await`/`yield`）限制在异步编程场景使用，在标记为`async`/`async*`/`sync*`同步和异步函数内部，禁止使用。

---
我的本博客原地址：[https://ntopic.cn/p/2023100201](https://ntopic.cn/p/2023100201/)

---
