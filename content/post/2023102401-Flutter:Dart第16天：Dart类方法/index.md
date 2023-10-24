+++
slug = "2023102401"
date = "2023-10-24"
lastmod = "2023-10-24"
title = "Flutter/Dart第16天：Dart类方法"
description = "类方法就是为对象提供一些数据操作的函数，也就是类方法是操作对象的函数。那么在Dart中，类方法到底有哪些特别之处呢……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "类" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/methods](https://dart.dev/language/methods)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## 实例方法
实例方法就是在类中定义的函数。对象的实例方法可访问`this`实例和实例变量。如下代码样例，`distanceTo()`函数就是一个实例方法：

```dart
import 'dart:math';

class Point {
  final double x;
  final double y;

  Point(this.x, this.y);

  double distanceTo(Point other) {
    var dx = x - other.x;
    var dy = y - other.y;
    return sqrt(dx * dx + dy * dy);
  }
}
```

## 操作符
操作符是一种具有特殊名字的实例方法。Dart语言允许我们定义如下操作符：

![](10.jpg)

<b>特别注意：</b>从上面的操作符列表中可以看到，Dart允许我们定义`<=` `>=` `==`操作符，但是为啥不能有 `!=` 呢？原因是 `!=`其实是一个语法糖。比如，表达式`object1 != object2`其实是 `!(object1 == object2)` 的语法糖。

我们通过内置的标识符`operator`来申明一个操作符。如下代码样例，`Vector`类申明了加(`+`)，减(`-`)和相等(`==`)三个操作符（或者方法）：

```dart
class Vector {
  final int x, y;

  Vector(this.x, this.y);

  Vector operator +(Vector v) => Vector(x + v.x, y + v.y);
  Vector operator -(Vector v) => Vector(x - v.x, y - v.y);

  @override
  bool operator ==(Object other) =>
      other is Vector && x == other.x && y == other.y;

  @override
  int get hashCode => Object.hash(x, y);
}

void main() {
  final v = Vector(2, 3);
  final w = Vector(2, 2);

  assert(v + w == Vector(4, 5));
  assert(v - w == Vector(0, 1));
}
```

## Getters和Setters方法
Getters和Setters是读写对象属性的特殊方法，其实每一个实例变量都隐含有一个Getter方法（如：object.x）和一个Setter方法（如：object.x = 'xxx'）。

如下代码样例，我们可以通过`get`和`set`关键字实现Getters和Setters方法，从而能提供额外属性：

```dart
class Rectangle {
  double left, top, width, height;

  Rectangle(this.left, this.top, this.width, this.height);

  // `right` 额外属性
  double get right => left + width;
  set right(double value) => left = value - width;

  // `bottom` 额外属性
  double get bottom => top + height;
  set bottom(double value) => top = value - height;
}

void main() {
  var rect = Rectangle(3, 4, 20, 15);
  assert(rect.left == 3);
  rect.right = 12;
  assert(rect.left == -8);
}
```

## 抽象方法
抽象方法仅仅可以在`abstract class`或者`mixin`中申明，通过分号`;`代替方法体即可。抽象方法包括实例方法、getters和setteers等。

如下代码样例，`Doer`抽象类申明了`doSomething()`抽象方法，`EffectiveDoer`子类实现了该抽象方法：

```dart
abstract class Doer {
  // 定义抽象方法
  void doSomething();
}

class EffectiveDoer extends Doer {
  // 实现抽象方法
  void doSomething() {
    // ...
  }
}
```

---
我的本博客原地址：[https://ntopic.cn/p/2023102401](https://ntopic.cn/p/2023102401/)

---
