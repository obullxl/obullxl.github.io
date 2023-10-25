+++
slug = "2023102501"
date = "2023-10-25"
lastmod = "2023-10-25"
title = "Flutter/Dart第17天：Dart类继承"
description = "Dart语言和其他面向对象语言一样，子类可以继承父类，获得父类的属性和方法。那么Dart语言，类继承还有什么特性呢……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "类" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/extend](https://dart.dev/language/extend)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## 类继承（extends/super）
Dart语言和Java语言一样，也是通过`extends`关键字创建子类，通过`super`关键字引用父类：

```dart
class Television {
  void turnOn() {
    _illuminateDisplay();
    _activateIrSensor();
  }
  // ···
}

// `extends`继承父类
class SmartTelevision extends Television {
  void turnOn() {
    // `super`引用父类
    super.turnOn();
    _bootNetworkInterface();
    _initializeMemory();
    _upgradeApps();
  }
  // ···
}
```

## 成员重写（override）
子类可以重写父类的成员方法，包括操作符、getters和setters等。通过`@override`注解表明重写父类的成员方法：

```dart
class Television {
  // ···
  set contrast(int value) {...}
}

class SmartTelevision extends Television {
  @override
  set contrast(num value) {...}
  // ···
}
```

子类重写方法的申明必须与父类被重新的方法相匹配，匹配的方式有以下几种：
 - 返回类型必须与重写方法的返回类型相同（或<b>子类型</b>）（如：父类方法返回类型是`num`，那么子类的返回类型必须是`num`或子类，如`int`等）。
 - 参数类型必须与重写方法的参数类型相同（或<b>超类型</b>）（如：上诉代码样例，`SmartTelevision`子类的参数类型`num`是父类`int`的超类）。
 - 位置参数的数量必须相同（如：父类接收3个位置参数，则子类必须也是3个位置参数）。
 - 泛型方法不能重写非泛型方法，反之也一样，非泛型方法不能重写泛型方法。

<b>最佳实战：</b>重写方法时，尽量避免缩写参数类型的范围，即尽量避免参数发生向下转换（如父类是num类型，而子类是int类型等），因为这样做可能会引发类型转换错误。当然，如果我们确定不会发生错误，也可以这样做。

<b>特别注意：</b>当我们重写了相等`==`操作符，则必须重写`hashCode`的`getter`方法：

```dart
class Person {
  final String firstName, lastName;

  Person(this.firstName, this.lastName);

  // 重写 `hashCode` 获取方法
  @override
  int get hashCode => Object.hash(firstName, lastName);

  // 重写 `==` 操作符
  @override
  bool operator ==(Object other) {
    return other is Person &&
        other.firstName == firstName &&
        other.lastName == lastName;
  }
}

void main() {
  var p1 = Person('Bob', 'Smith');
  var p2 = Person('Bob', 'Smith');
  var p3 = 'not a person';
  assert(p1.hashCode == p2.hashCode);
  assert(p1 == p2);
  assert(p1 != p3);
}
```

## noSuchMethod()方法
若需要在访问不存在的方法或实例变量时，我们代码能做出响应（而不是抛出`NoSuchMethodError`错误），则我们可以重写`noSuchMethod()`方法：

```dart
class A {
  // 重写`noSuchMethod`方法，避免`NoSuchMethodError`错误
  @override
  void noSuchMethod(Invocation invocation) {
    print('You tried to use a non-existent member: '
        '${invocation.memberName}');
  }
}
```

在Dart语言中，除了以下几种情况外，我们不可能调用一个不存在的方法（编译就出错）：
 - 对象是`dynamic`动态类型，运行时才能确定具体类型。
 - 对象是静态类型，存在未实现的方法，且它实现了`noSuchMethod()`方法（即它不是继承`Object`类型的`noSuchMethod()`方法）。

---
我的本博客原地址：[https://ntopic.cn/p/2023102501](https://ntopic.cn/p/2023102501/)

---
