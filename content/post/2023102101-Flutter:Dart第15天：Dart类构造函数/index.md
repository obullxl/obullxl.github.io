+++
slug = "2023102101"
date = "2023-10-21"
lastmod = "2023-10-21"
title = "Flutter/Dart第15天：Dart类构造函数"
description = "我们通过类构造函数来创建对象，上文（第14天）我们学到，与Java不同，创建Dart对象时可以省略构造函数之前的new关键字。同时，Dart语言除默认构造函数外，还有命名构造函数，重定向构造函数，常量构造函数和工厂构造函数等……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "类" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/constructors](https://dart.dev/language/constructors)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

如下代码样例，和Java类似，最常用的生成式构造函数：

```dart
class Point {
  double x = 0;
  double y = 0;

  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
}
```

<b>最佳实战：</b>在Dart中，仅当命名冲突时，才使用this关键字，否则一般可以省略`this`关键字。

## 初始化参数列表
如上最常用的构造函数，Dart可以进一步优化如下初始化参数形式。同时，也可以为非空变量设置默认值。

```dart
class Point {
  final double x;
  final double y;

  // 在构造函数体执行之前，初始化实例变量
  Point(this.x, this.y);
}
```

## 默认构造函数
和Java类似，类如果没有申明构造函数，那么它会有个默认的构造函数。默认构造函数没有入参，它只会调用父类的没有入参的构造函数。

## 构造函数无法继承
子类无法继承父类的构造函数，如果子类没有申明构造函数，那么这个子类就只有默认构造函数（无论父类是否有其他构造函数）。

## 命名构造函数
在Dart中，通过命名构造函数，可以为类提供多个构造函数，并且在创建对象时更加清晰。

```dart
const double xOrigin = 0;
const double yOrigin = 0;

class Point {
  final double x;
  final double y;

  Point(this.x, this.y);

  // `origin`命名构造函数
  Point.origin()
      : x = xOrigin,
        y = yOrigin;
}
```

<b>特别注意：</b>如上节提到，子类无法继承父类的构造函数，包括父类的命名构造函数。如果子类想使用父类的某个命名构造函数，那么子类必须实现该命名构造函数。

## 调用父类构造函数
默认情况下，子类无入参的非命名构造函数会调用父类的无入参的非命名构造函数。父类的构造函数在构造函数体之前被调用。如果有初始化参数列表，那么初始化参数列表在父类构造函数调用之前被调用。

构造函数相关的调用顺序如下：
 - 初始化参数列表
 - 父类无入参的构造函数
 - 子类无入参的构造函数

<b>特别注意：</b>如果父类没有通过无入参且非命名的构造函数，那么我们必须手工调用父类的一个构造函数，通过冒号`:`后面紧跟父类构造函数。

如下代码样例，`Person`是父类，它仅申明了一个命名构造参数。`Employee`是继承`Person`的子类，由于父类没有申明无入参非命名的构造函数，因此在它构造函数都必须手工调用父类的某个构造函数。如命名构造函数`fromJson`后面，通过冒号`:`调用了父类的命名构造函数。

```dart
// 未申明：无入参、非命名的构造函数
class Person {
  String? firstName;

  Person.fromJson(Map data) {
    print('in Person');
  }
}

class Employee extends Person {
  // 手工调用父类的构造函数：super.fromJson()
  Employee.fromJson(super.data) : super.fromJson() {
    print('in Employee');
  }
}

void main() {
  var employee = Employee.fromJson({});
  print(employee);
  // 结果：
  // in Person
  // in Employee
  // Instance of 'Employee'
}
```

<b>特别注意：</b>由于构造函数参数是在调用构造函数之前计算，因此构造函数的参数可以是表达式，如函数调用等。父类的构造函数不能使用`this.`关键字，因为参数可以是表达式、静态函数等，并不一定是类实例。

```dart
class Employee extends Person {
  Employee() : super.fromJson(fetchDefaultData());
  // ···
}
```

手工调用父类的构造函数，然后逐一设置入参比较繁琐，如果我们想要简化，那么可以父类的初始值构造函数。这个功能不能与重定向构造函数一起使用（因为语法冲突）。

```dart
class Vector2d {
  final double x;
  final double y;

  Vector2d(this.x, this.y);
}

class Vector3d extends Vector2d {
  final double z;

  // 默认情况下，我们的使用方法：
  // Vector3d(final double x, final double y, this.z) : super(x, y);
  
  // 简化版本：
  Vector3d(super.x, super.y, this.z);
}
```

如下代码样例，父类的初始化构造函数可以通过命名参数调用。

```dart
class Vector2d {
  // ...

  Vector2d.named({required this.x, required this.y});
}

class Vector3d extends Vector2d {
  // ...

  Vector3d.yzPlane({required super.y, required this.z}) : super.named(x: 0);

  // 等价调用
  // Vector3d.yzPlane({required double y, required this.z}) : super.named(x: 0, y: y);
}
```

## 初始化列表
在构造函数执行之前，我们可以调用父类的构造函数，还可以初始化实例变量。实例变量初始化通过逗号分隔。

```dart
Point.fromJson(Map<String, double> json)
    : x = json['x']!,
      y = json['y']! {
  print('In Point.fromJson(): ($x, $y)');
}
```

开发阶段，我们可以在初始化列表中增加断言：

```dart
Point.withAssert(this.x, this.y) : assert(x >= 0) {
  print('In Point.withAssert(): ($x, $y)');
}
```

初始化列表在设置`final`不可变量时非常有用：

```dart
import 'dart:math';

class Point {
  final double x;
  final double y;
  final double distanceFromOrigin;

  Point(double x, double y)
      : x = x,
        y = y,
        distanceFromOrigin = sqrt(x * x + y * y);
}

void main() {
  var p = Point(2, 3);
  print(p.distanceFromOrigin);
  // 输出：3.605551275463989
}
```

## 重定向构造函数
重定向构造函数，就是使用类的其他的构造函数，重定向到的构造函数使用`this`关键字：

```dart
class Point {
  double x, y;

  // 主构造函数
  Point(this.x, this.y);

  // 重定向到主构造函数
  Point.alongXAxis(double x) : this(x, 0);
}
```

## 常量构造函数
如果对象的数据不会改变，这些对象可以作为编译期常量。

常量构造函数<b>要求：</b>实例变量都是`final`不可变量，定义一个`const`修饰符的构造函数。

<b>特别注意：</b>上一文我们有提到常量构造函数，常量构造函数创建的对象并<b>不一定<?都是常量（当创建的对象没有`const`修饰符，或者对象不是在`const`常量上下文中，那么该对象就不是常量）！

如下代码样例，`ImmutablePoint`有常量构造函数，它创建的3个对象中，前面2个是常量，后面1个并非常量。

```dart
class ImmutablePoint {
  static const ImmutablePoint origin = ImmutablePoint(0, 0);

  final double x, y;

  const ImmutablePoint(this.x, this.y);
}

// `a`和`b`对象是常量，且它们属于同一个实例
var a = const ImmutablePoint(1, 2);
var b = const ImmutablePoint(1, 2);

assert(identical(a, b));

// `c`对象并非常量，它也和`a`或者`b`不是同一个实例
var c = ImmutablePoint(1, 2);
assert(!identical(a, b));
```

## 工厂构造函数
在一个不总是创建实例的类中，使用`factory`关键字实现一个构造函数，即为工厂构造函数。

工厂构造函数常用<b>使用场景：</b>如通过构造函数，从缓存获取对象，或者创建其子类，或者创建不可变常量但是又不想提供初始化参数列表等。

如下代码样例，`Logger`工厂构造函数优先从缓存获取对象，而`Logger.fromJson()`工厂构造函数则初始化了一个`final`不可实例变量：

```dart
class Logger {
  final String name;
  bool mute = false;

  static final Map<String, Logger> _cache = <String, Logger>{};

  // 优先从缓存获取对象，如果不存在则新增
  factory Logger(String name) {
    print('默认构造函数：$name');
    return _cache.putIfAbsent(name, () => Logger._internal(name));
  }

  // 命名构造函数，通过工厂构造函数获取对象（缓存，或新增）
  factory Logger.fromJson(Map<String, Object> json) {
    print('命名构造函数：$json');
    return Logger(json['name'].toString());
  }

  Logger._internal(this.name);

  void log(String msg) {
    if (!mute) print(msg);
  }
}
```

工厂构造函数的使用，和普通构造函数无本质区别：

```dart
var logger = Logger('UI');
logger.log('Hi NTopicCN.');
// 结果：
// 默认构造函数：UI
// Hi NTopicCN.

var loggerJson = Logger.fromJson({'name': 'UI'});
loggerJson.log('Hello Logger.');
// 结果：
// 命名构造函数：{name: UI}
// 默认构造函数：UI
// Hello Logger.
```

---
我的本博客原地址：[https://ntopic.cn/p/2023102101](https://ntopic.cn/p/2023102101/)

---
