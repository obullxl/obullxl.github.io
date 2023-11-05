+++
slug = "2023110501"
date = "2023-11-05"
lastmod = "2023-11-05"
title = "Flutter/Dart第20天：Dart 3.0新特性之类型修饰符"
description = "Dart 3.0版本新增了很多新特性，包括有名的健全的空安全；同时针对类型（包括Mixin），除之前的abstract修饰符之外，还增加了base，final，interface和sealed等修饰符。今天我们来一起看下，这些类型修饰符，它们有哪些使用场景、使用时有哪些约束，和如何组合使用……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "类" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/class-modifiers](https://dart.dev/language/class-modifiers)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

类型修饰符主要是控制类或者Mixin如何被使用，包括在库内部和外部使用。修饰符关键字出现在类型或Mixin申明的前面，如`abstract class`通过`abstract`修饰符定义了一个抽象类。

可用于声明类的修饰符关键字列表如下：
 - abstract
 - base
 - final
 - interface
 - sealed
 - mixin

<b>约束：</b>上面的修饰符列表，只有`base`能用于Mixin类型；同时，上诉修饰符不能用于包括`enum`、`typedef`和`extension`等类型声明。

<b>实战：</b>当我们决定要使用修饰符时，可能需要考虑一下类的预期用途和类需要提供哪些行为。

## 无修饰符
当我们定义类或者Mixin时，不希望对构造函数或者子类进行限制时，我们可以不使用修饰符。

当类或者Mixin没有修饰符时，默认情况下，可以对这些类或者Mixin进行以下操作：
 - 通过构造函数创建类实例
 - 通过继承类来创建子类
 - 实现类或者Mixin的接口
 - 混入Mixin或者Mixin类

## abstract修饰符（抽象类）
<b>使用场景：</b>当我们定义了一个类（即：抽象类），但又没有完整地实现了它所有的接口时使用（和Java语言一样），请使用`abstract`修饰符。

<b>约束：</b>抽象类不能被实例化；一般情况，抽象类都包含抽象方法。

```dart
// 抽象类
abstract class Vehicle {
  void moveForward(int meters);
}

// 实现类
class MockVehicle implements Vehicle {
  @override
  void moveForward(int meters) {
    // ...
  }
}
```

## base修饰符（基类）
<b>使用场景：</b>当我们用`base`修饰符定义了一个类或者Mixin时（即：基类），那么这个基类的实现只能基类所在库内。这样做的目的：
 - 每当创建子类实例时，基类的构造函数被调用
 - 所有已经实现的私有成员都在子类中
 - 在基类中新增加的成员会被所有子类继承（除非：子类中申明了同名的成员但并不兼容的签名。如：子类申明了同名方法，但是方法入参或者返回结果与基类不兼容）

<b>实战：</b>为了保证基类不会被破坏，子类必须使用`base`，`final`或者`sealed`修饰符。

如下代码样例，基类可以实例化、被继承，但是不能被实现：

```dart
// 基类
base class Vehicle {
  void moveForward(int meters) {
    // ...
  }
}

// 1. 实例化
Vehicle myVehicle = Vehicle();

// 2. 被继承
base class Car extends Vehicle {
  int passengers = 4;
  // ...
}

// 3. ERROR：不能被实现
base class MockVehicle implements Vehicle {
  @override
  void moveForward() {
    // ...
  }
}
```

## interface修饰符（接口类）
<b>使用场景：</b>使用`interface`修饰符定义一个接口。接口可以被外部库实现，但是不能被继承。这样做的目的：
 - 当类的一个实例方法使用`this`调用另一个实例方法时，它总是调用同一个库的实例方法
 - 为了避免不可预期的方法调用，其他库不能重新接口已有的方法

如下代码样例，接口类可以实例化、被实现，但是不能被继承：

```dart
// a.dart 接口类
interface class Vehicle {
  void moveForward(int meters) {
    // ...
  }
}

//
// b.dart
//
import 'a.dart';

// 1. 实例化
Vehicle myVehicle = Vehicle();

// 2. 被实现
class MockVehicle implements Vehicle {
  @override
  void moveForward(int meters) {
    // ...
  }
}

// 3. ERROR: 不能被继承
class Car extends Vehicle {
  int passengers = 4;
  // ...
}
```

## abstrace interface（抽象接口类）
一般情况下，我们使用`interface`来定义纯粹接口。

当我们使用`abstract interface class`组合修饰符时，可以定义一个抽象接口类：它即有接口类的功能（可被实现，但不能被继承），也有抽象类的功能（有抽象成员）。

## final修饰符（不可变类）
<b>使用场景：</b>当使用`final`修饰符时，表示该类不能被其他库继承和实现（和Java还有点不一样）。这样做的目的：
 - 可以安全地进行API变更
 - 该类不会被第三方子类覆盖，因此可以放心调用实例方法

<b>约束：</b>`final`不可变类可以在本库中被继承和实现，`final`修饰符包含了`base`修饰符特性，因此，子类必须使用`base`，`final`或者`sealed`修饰符。

```dart
// a.dart 接口类
final class Vehicle {
  void moveForward(int meters) {
    // ...
  }
}

//
// b.dart
//
import 'a.dart';

// 1. 实例化
Vehicle myVehicle = Vehicle();

// 2. ERROR: 不能被继承
class Car extends Vehicle {
  int passengers = 4;
  // ...
}

class MockVehicle implements Vehicle {
  // 3. ERROR: 不能被实现
  @override
  void moveForward(int meters) {
    // ...
  }
}
```

## sealed修饰符（密封类）
<b>使用场景：</b>当我们定义了一个类（即：密封类），且明确该类的所有子类集合时，请使用`sealed`修饰符。这允许我们通过`switch`穷举所有的子类型。

<b>约束：</b>`sealed`修饰的类，禁止被其他库继承或者实现，它隐含`abstract`修饰符：
 - 不能被实例化
 - 可以有工厂构造函数
 - 可以定义构造函数，子类可直接使用
 - 子类并不是`abstract`抽象类

编译器可以知道所有`sealed`修饰符类的子类（因为他们在同一个库中），这样在`switch`中，如未穷举，编译器能发出错误警告！

```dart
// 密封类
sealed class Vehicle {}

class Car extends Vehicle {}

class Truck implements Vehicle {}

class Bicycle extends Vehicle {}

// 1. ERROR: 不能被实例化
Vehicle myVehicle = Vehicle();

// 2. 子类可以被实例化
Vehicle myCar = Car();

String getVehicleSound(Vehicle vehicle) {
  // 3. ERROR: switch中子类未穷举（还有Bicycle子类）
  return switch (vehicle) {
    Car() => 'vroom',
    Truck() => 'VROOOOMM',
  };
}
```

在`switch`中，如果我们不想穷举`sealed`类的子类；又或者以后还会增加子类，但又不想破坏API设计，我也可以使用`final`修饰符。关于`final`和`sealed`修饰符的深入比较，请稍等本博客的下一个博客介绍（请容许我卖个关子！）。

## 组合修饰符
通过组合修饰符，可以起到叠加限制效果。我们申明类时，按照顺序，可以叠加的修饰符：
 - 可选的`abstract`修饰符：类包含抽象成员，且不能被实例化
 - 可选的`base`、`interface`、`final`和`sealed`修饰符：限制其他库的子类型
 - 可选的`mixin`修饰符：类是否可被混入
 - 必选的`class`类关键字

部分修饰符是不能组合使用，因为他们可能多余或者矛盾互斥：
 - `abstract`修饰符和`sealed`修饰符：原因是`sealed`隐含了`abstract`修饰符
 - `interface`、`final`或`sealed`修饰符和`mixin`修饰符：原因是这些修饰符都禁止被混入

完整的有效的修饰符组合列表如下：

![](11.jpg)

---
我的本博客原地址：[https://ntopic.cn/p/2023110501](https://ntopic.cn/p/2023110501/)

---
