+++
slug = "2023100601"
date = "2023-10-06"
lastmod = "2023-10-06"
title = "Flutter/Dart第11天：Dart函数方法详解"
description = "Dart语言是纯面向对象的编程语言，就算是函数（对象的成员函数一般称为方法）也是对象，它也有类型，那么函数也可以作为其他函数的参数，或者赋值给其他变量。除此之外，Dart中的函数还有什么特别之处、它有什么规则和约束……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "函数方法" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/functions](https://dart.dev/language/functions)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

Dart语言是纯面向对象的编程语言，就是是函数也是对象，它的类型就是`Function`类（[https://api.dart.dev/stable/3.1.3/dart-core/Function-class.html](https://api.dart.dev/stable/3.1.3/dart-core/Function-class.html)）。

如下代码样例，函数的不同实现。如果函数实现仅仅只有<b>1个</b>表达式，那么函数可以使用<b>箭头</b>语法：`=> return expression;`

```dart
// 函数实现
bool isNoble(int atomicNumber) {
  return _nobleGases[atomicNumber] != null;
}

// 箭头语法，上诉函数的简单语法
bool isNoble(int atomicNumber) => _nobleGases[atomicNumber] != null;
```

## 命名参数（必选，默认值）
<b>参数格式：</b>`functionName({param1 = value1, param2, ...})`

<b>函数调用：</b>`functionName(param1: valueX, param2: value2, ...)`

默认情况下，命名参数是<b>可选</b>的，除非显示增加`required`标记。在Flutter中，尤其是Widget是构造函数，仅仅使用命名参数，尽管参数是必填参数。

<b>特别注意：</b>就算了增加`required`的必选参数，它也可以是可空的。

## 位置参数（可选，默认值）
可选位置参数通过`[]`包裹的参数列表，默认值为`null`，可以设置默认值：

<b>参数格式：</b>`functionName(param1, param2, [param3 = value3, param4]);`

<b>函数调用：</b>`functionName(value1, value2);或functionName(value1, value2, value3);或functionName(value1, value2, value3, value4);`

## main()函数
任何Dart应用，都必须包含一个顶级`main()`函数，它是应用的唯一入口。它的返回值是`void`，入参是`List<String>`类型。

<b>代码样例：</b>如下代码，我们在执行Dart文件时，指定了参数`dart args.dart 1 test`

```dart
void main(List<String> arguments) {
  print(arguments);

  assert(arguments.length == 2);
  assert(int.parse(arguments[0]) == 1);
  assert(arguments[1] == 'test');
}
```

## 函数作为参数
函数可以作为其他函数的入参，也可以赋值给变量。

```dart
// 1. 函数作为函数入参
void printElement(int element) {
  print(element);
}

var list = [1, 2, 3];
list.forEach(printElement);

// 2. 函数赋值给变量（匿名函数）
var loudify = (msg) => '!!! ${msg.toUpperCase()} !!!';
assert(loudify('hello') == '!!! HELLO !!!');
```

## 匿名函数
如上代码，`main()`和`printElement()`函数都是<b>命名函数</b>。我们也可以创建<b>匿名函数</b>（如上代码`loudify`变量的值），特别是在`Lambda表达式`或者在`闭包函数`中，匿名函数使用场景很多。

<b>代码样例：</b>如下代码，`map()`和`forEach()`的入参就是匿名函数。

```dart
void main() {
  const list = ['apples', 'bananas', 'oranges'];
  list
    .map((item) => item.toUpperCase())
    .forEach((item) => print('$item: ${item.length}'));
}
```

## 函数相等校验
可以进行相等校验的函数包括：顶级函数，静态函数和实例函数。

```dart
// 1. 顶级函数
void foo() {}

class A {
  // 2. 静态方法
  static void bar() {}
  // 3. 实例方法
  void baz() {}
}

void main() {
  Function x;

  // 顶级函数相等校验
  x = foo;
  assert(foo == x);

  // 静态方法相等校验
  x = A.bar;
  assert(A.bar == x);

  // 实例方法相等校验
  var v = A(); // A实例#1
  var w = A(); // A实例#2
  var y = w;
  x = w.baz;

  // 同是A实例#2方法
  assert(y.baz == x);

  // 不同实例方法
  assert(v.baz != w.baz);
}
```

## 函数返回值
如果没有显示返回值，函数默认返回`null`，Record记录可以聚合返回多个值。

```dart
(String, int) foo() {
  return ('something', 42);
}
```

## 生成器函数
生成器函数可以延迟产出一系列值，Dart中内置2类生成器函数：
1. 同步生成器：返回1个<b>Iterable</b>对象。
2. 异步生成器：返回1个<b>Stream</b>对象。

<b>同步生成器</b>函数：使用`sync*`标记函数体，并且使用`yield`表达式产生值。

```dart
Iterable<int> naturalsTo(int n) sync* {
  int k = 0;
  while (k < n) yield k++;
}
```

<b>异步生成器</b>函数：使用`async*`标记函数体，并且使用`yield`表达式产生值。

```dart
Stream<int> asynchronousNaturalsTo(int n) async* {
  int k = 0;
  while (k < n) yield k++;
}
```

如果生成器是<b>递归</b>的，可以通过`yield*`来提升性能。

```dart
Iterable<int> naturalsDownFrom(int n) sync* {
  if (n > 0) {
    yield n;
    yield* naturalsDownFrom(n - 1);
  }
}
```

---
我的本博客原地址：[https://ntopic.cn/p/2023100601](https://ntopic.cn/p/2023100601/)

---
