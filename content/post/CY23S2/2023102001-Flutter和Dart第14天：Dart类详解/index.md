+++
slug = "2023102001"
date = "2023-10-20"
lastmod = "2023-10-20"
title = "Flutter/Dart第14天：Dart类详解"
description = "通过前面13天的学习，对Dart基础有了系统的熟悉，今天我们开始学习Dart类和对象，本文主要学习Dart类，包括类方法，构造器，对象类型，实例变量，隐性接口，类变量和类方法等……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "类" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/classes](https://dart.dev/language/classes)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## Dart类
Dart语言基于<b>类</b>和<b>Mixin</b>继承，是一门面向对象语言。任何对象都是某个类的实例，除`Null`之外，`Object`类其他所有类的父类。

<b>Mixin继承：</b>Dart语言和Java语言一样，类只能是单继承。但通过Mixin，一个类的代码可以在多个类层次结构中复用（有关<b>Minxin的详细说明</b>见之前文章：[https://ntopic.cn/p/2023093001](https://ntopic.cn/p/2023093001)）。

<b>方法扩展：</b>在不改变原有类和增加子类的情况之下，通过Dart的<b>方法扩展</b>，可以给类增加功能的一种方法（这个特性在Flutter发布的库特别有用）。

<b>类修饰符：</b>可以让我们可控制一个库如果定义子类。

## 类成员（方法和变量）
对象是由函数和数据组成，分别代码方法和变量。我们通过`对象.方法`或者`对象.变量`的方法来访问对象方法和变量。当对象可能为`null`时，通过`对象.?`访问方法和变量的方式可防止异常发生。

```dart
// 定义对象
var p = Point(2, 2);

// 获取对象变量`y`
assert(p.y == 2);

// 调用对象方法：`distanceTo()`
double distance = p.distanceTo(Point(4, 4));

// 当对象`p`非空时，`a`值为变量`y`；否则`a`值为null
var a = p?.y;
```

## 类构造函数
在前面学习中，我们对构造函数有初步认识：[https://ntopic.cn/p/2023092401](https://ntopic.cn/p/2023092401)

如下代码样例，可以通过主构造函数和命名构造函数创建一个对象；构造函数之前，我们也可以增加可选的`new`关键字：

```dart
var p1 = Point(2, 2);
var p2 = Point.fromJson({'x': 1, 'y': 2});

// 同上等价代码，可选的`new`关键字
var p1 = new Point(2, 2);
var p2 = new Point.fromJson({'x': 1, 'y': 2});
```

当类的变量都用`final`不可变修饰时，我们可以构造常量对象：

```dart
var a = const ImmutablePoint(1, 1);
var b = const ImmutablePoint(1, 1);

// 对象`a`和`b`相等
assert(identical(a, b));
```

对于一个常量上下文，我们可以去掉构造函数之前的`const`关键字。如下代码样例，我们定义的是一个常量Map（上下文），那么Map元素的构造器就可以省略`const`关键字：

```dart
const pointAndLine = const {
  'point': const [const ImmutablePoint(0, 0)],
  'line': const [const ImmutablePoint(1, 10), const ImmutablePoint(-2, 11)],
};

// 同上等价代码，可省略`const`关键字
const pointAndLine = {
  'point': [ImmutablePoint(0, 0)],
  'line': [ImmutablePoint(1, 10), ImmutablePoint(-2, 11)],
};
```

如果一个对象没有常量上下文，且没有使用`const`修饰构造器，那么它创建的是一个非常量对象：

```dart
var a = const ImmutablePoint(1, 1);
var b = ImmutablePoint(1, 1);

// `a`是常量对象，`b`不是常量对象，因此它们不相等！
assert(!identical(a, b));
```

## 获取对象类型
通过`对象.runtimeType`属性，返回对象的`Type`对象。一般情况下，我们通过`对象 is Type`的方法，检测某个对象是否属于某个类型，而不是使用`对象.runtimeType == Type`比较方式：

```dart
print('The type of a is ${a.runtimeType}');

var a = 'Hello NTopicCN';
assert(a.runtimeType == String);
assert(a is String);
```

## 实例变量
如下代码定义样例，申明实例变量，实例变量的默认值为`null`：

```dart
class Point {
  double? x; // 默认值：null
  double? y; // 默认值：null
  double z = 0; // 默认值：0
}
```

所有的实例变量都隐含有一个`getter`方法，包括<b>final修饰的变量</b>、<b>未使用final修饰的变量</b>、<b>late final修饰的变量</b>（赋值和未赋值）等，都有getter方法。

如下代码样例，几种实例变量修饰和访问的方法：

```dart
class Point {
  double? x;
  double? y;
}

void main() {
  var point = Point();
  point.x = 4; // `setter`方法赋值
  assert(point.x == 4); // `getter`方法取值
  assert(point.y == null); // 默认值为`null`
}

class ProfileMark {
  final String name;
  final DateTime start = DateTime.now();

  // 主构造函数
  ProfileMark(this.name);

  // 命名构造函数，同时`name`设置初始值
  ProfileMark.unnamed() : name = '';
}
```

## 隐性接口
在Dart中，每个类都隐含的定义了一个接口，这个接口包含了该类的所有实例成员和该类实现的所有的其他接口。

假设我们定义了一个类A，它需要支持类B的API（构造函数不是API），但是类A的定义并不是继承类B，那么类A需要实现B接口。

```dart
// `Person`类，也是`Persion`接口，包含`greet()`方法
class Person {
  // 属于接口的一部分，但是对外不可见
  final String _name;

  // 构造函数，不属于接口一部分
  Person(this._name);

  // 普通方法，属于接口一部分
  String greet(String who) => 'Hello, $who. I am $_name.';
}

// 实现`Person`接口
class Impostor implements Person {
  String get _name => '';

  String greet(String who) => 'Hi $who. Do you know who I am?';
}

String greetBob(Person person) => person.greet('Bob');

void main() {
  print(greetBob(Person('Kathy'))); // Hello, Bob. I am Kathy.
  print(greetBob(Impostor())); // Hi Bob. Do you know who I am?
}
```

## 类变量和方法
`static`关键字，可以定义类变量和方法（Java中成为静态变量和静态方法）。

如下代码样例，定义和使用一个类变量：

```dart
class Queue {
  static const initialCapacity = 16;
  // ···
}

void main() {
  assert(Queue.initialCapacity == 16);
}
```

如下代码样例，定义和使用类方法：

```dart
import 'dart:math';

class Point {
  double x, y;
  Point(this.x, this.y);

  static double distanceBetween(Point a, Point b) {
    var dx = a.x - b.x;
    var dy = a.y - b.y;
    return sqrt(dx * dx + dy * dy);
  }
}

void main() {
  var a = Point(2, 2);
  var b = Point(4, 4);
  var distance = Point.distanceBetween(a, b);
  assert(2.8 < distance && distance < 2.9);
  print(distance);
}
```

<b>最佳实践：</b>对于一些常用的工具方法，建议使用<b>顶级方法</b>代替类变量。

类方法可以用作编译期常量，比如：我们可以把一个类方法当作参数传递给常量构造器。

---
我的本博客原地址：[https://ntopic.cn/p/2023102001](https://ntopic.cn/p/2023102001/)

---
