+++
slug = "2023101301"
date = "2023-10-13"
lastmod = "2023-10-13"
title = "Flutter/Dart第12天：Dart控制流详解"
description = "本文是Dart语言学习的第12天，和前面11天相比，本文可能相对比较简单，因为本文要学习的是Dart语言的控制流，也就是循环和分支。且前面的11天学习中，多多少少都涉及到了控制流，同时对Java或者JavaScript比较熟悉的朋友，可能比较容易上手……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "控制流" ]
categories = [ "专业技术" ]
+++

Dart控制流主要由<b>循环</b>和<b>分支</b>组成：
 - Dart官方文档-循环：[https://dart.dev/language/loops](https://dart.dev/language/loops)
 - Dart官方文档-分支：[https://dart.dev/language/branches](https://dart.dev/language/branches)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## 循环
Dart可通过循环来控制逻辑流，它支持3种语句：`for`循环，`while`和`do while`循环，`break`和`continue`等

同时，也可以通过非循环来控制逻辑流，包括：分支（如：`if`和`switch`）和异常（`try`、`catch`和`throw`）等

### for循环
<b>for循环</b>可以使用在迭代中，标准用法如下代码样例：

```dart
var message = StringBuffer('Dart is fun');
for (var i = 0; i < 5; i++) {
  message.write('!');
}
```

<b>for-in</b>循环常用于无需关注迭代的索引，如下代码样例，包含了闭包列表和迭代：

```dart
var callbacks = [];
for (var i = 0; i < 2; i++) {
  callbacks.add(() => print(i));
}

for (final c in callbacks) {
  c();
}
// 解构：01
```

同样的，<b>for-in循环</b>还可以使用对象<b>模式</b>（Pattern模式详解：[https://ntopic.cn/p/2023100501](https://ntopic.cn/p/2023100501)），解构迭代的元素：

```dart
for (final Candidate(:name, :yearsExperience) in candidates) {
  print('$name has $yearsExperience of experience.');
}
```

对于迭代类型（Dart可迭代集合详解：[https://ntopic.cn/p/2023092701](https://ntopic.cn/p/2023092701)），`forEach()`方法也可以实现循环：

```dart
var collection = [1, 2, 3];
collection.forEach(print); // 1 2 3
```

### while和do while循环
<b>while循环</b>和<b>do while循环</b>最大的区别：`while`<b>先检测</b>循环条件，<b>然后执行</b>循环逻辑；`do while`<b>默认先执行1次</b>循环逻辑，<b>然后检测</b>循环条件，以<b>决定后续</b>是否继续执行循环逻辑。

```dart
while (!isDone()) {
  doSomething();
}

do {
  printLine();
} while (!atEndOfPage());
```

### break和continue循环控制
<b>break</b>和<b>continue</b>在循环中的最大区别：`break`<b>中断整个</b>循环，而`continue`中断当前循环的<b>后续逻辑</b>。

```dart
while (true) {
  if (shutDownRequested()) break;
  processIncomingRequests();
}

for (int i = 0; i < candidates.length; i++) {
  var candidate = candidates[i];
  if (candidate.yearsExperience < 5) {
    continue;
  }
  candidate.interview();
}

// 若`candidates`是可迭代列表，则上面的逻辑可改写如下：
candidates
    .where((c) => c.yearsExperience >= 5)
    .forEach((c) => c.interview());
```

## 分支
Dart也可通过分支来控制逻辑流：`if`语句和元素，`if-case`语句和元素，`switch`语句和`switch`表达式等。

同样的，我们也可以通过其他方式来控制逻辑流：如循环（就是本文的第1节）和异常（`try`、`catch`和`throw`）等

### if/else分支
`if`语句可以有`else`可选项子句，`if`后面的括号中的表达式计算值必须是`boolean`类型，如下代码样例：

```dart
if (isRaining()) {
  you.bringRainCoat();
} else if (isSnowing()) {
  you.wearJacket();
} else {
  car.putTopDown();
}
```

### if-case模式
Dart中`if`语句后的`case`子句可以带一个<b>模式</b>（Pattern模式详解：[https://ntopic.cn/p/2023100501](https://ntopic.cn/p/2023100501)）：

```dart
if (pair case [int x, int y]) return Point(x, y);
```

上诉代码样例中，列表模式`[int x, int y]`首先匹配`pair`值，并且把列表第1和第2个`int`类型的元素解构并赋值给了`x`和`y`变量。

如果模式未能成功匹配，则需要进入`else`分支：

```dart
if (pair case [int x, int y]) {
  print('Was coordinate array $x,$y');
} else {
  throw FormatException('Invalid coordinates.');
}
```

### switch语句和switch表达式
`switch`语句后面可以有多个`case`子句，每一个`case`子句都可以是一个<b>模式</b>（Pattern模式详解：[https://ntopic.cn/p/2023100501](https://ntopic.cn/p/2023100501)）。

当没有`case`子句匹配时，就会执行`default`子句或者`_`通配符子句：

```dart
switch (command) {
  case 'OPEN':
    executeOpen();
    continue newCase; // 继续执行

  case 'DENIED':
  case 'CLOSED':
    executeClosed(); // `DENIED`和`CLOSED`均会执行

  newCase:
  case 'PENDING':
    executeNowClosed(); // `OPEN`和`PENDING`均会执行
}
```

在switch语句中，还可以使用模式，有如下几种用法：

```dart
var x = switch (y) { ... };

print(switch (x) { ... });

return switch (x) { ... };
```

<b>switch表达式</b>可以重写<b>switch语句</b>，如下为<b>switch语句</b>的代码样例：

```dart
// `slash`, `star`, `comma`, `semicolon`等，需要是常量
switch (charCode) {
  case slash || star || plus || minus: // `逻辑或`模式
    token = operator(charCode);
  case comma || semicolon: // `逻辑或`模式
    token = punctuation(charCode);
  case >= digit0 && <= digit9: // `关系`和`逻辑与`模式
    token = number();
  default:
    throw FormatException('Invalid');
}
```

上诉代码样例，可以使用<b>switch表达式</b>重写如下：

```dart
token = switch (charCode) {
  slash || star || plus || minus => operator(charCode),
  comma || semicolon => punctuation(charCode),
  >= digit0 && <= digit9 => number(),
  _ => throw FormatException('Invalid')
};
```

<b>switch表达式</b>不同于<b>switch语句</b>的语法，包括以下几个方面：
 - Case可选项无需使用`case`开头。
 - Case可选项的逻辑是一个<b>表达式</b>，而不是一系列的语句。
 - Case可选项都必须有<b>逻辑</b>，空可选性不代表隐性失败。
 - Case可选项模式，逻辑使用`=>`分割。
 - 多个Case可选项之间，使用`,`分割。
 - 默认可选项，只能使用`_`。

Switch还可以使用在<b>穷举检测</b>中，就是所有的值都有对应的可选项被处理，以避免引发编译错误。默认可选项（`default`或`_`）涵盖了所有其他情况，因此它可以保障穷举性。

### case-when子句
<b>case-when子句：</b>在`case`子句后面增加`when`子句。<b>case-when子句</b>可以用在<b>if-case</b>、<b>switch语句</b>和<b>switch表达式</b>中。

```dart
switch (pair) {
  case (int a, int b) when a > b:
    print('First element greater');
  case (int a, int b):
    print('First element not greater');
}
```

<b>case-when子句</b>计算任何一个`boolean`类型的值，值为`true`代表可以执行本Case可选项逻辑，为`false`则<b>继续执行</b>下一个Case可选项，并不会退出整个Switch语句。

---
我的本博客原地址：[https://ntopic.cn/p/2023101301](https://ntopic.cn/p/2023101301/)

---
