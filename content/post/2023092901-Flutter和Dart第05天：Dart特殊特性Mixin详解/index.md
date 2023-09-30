+++
slug = "2023093001"
date = "2023-09-30"
lastmod = "2023-09-30"
title = "Flutter/Dart第05天：Dart特殊特性Mixin详解"
description = "在Java语言中，子类只能继承extends单个父类，实现implements多个接口（即单继承和多实现）。在Dart语言中，所有类型均是Object子类，它们也是单继承和多实现，但Dart中有个Mixin的高级特性，它可以做到更多的代码复用（单继承、多实现、多Mixin代码复用）……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "Mixin", "继承" ]
categories = [ "专业技术" ]
+++

Dart官网文档：[https://dart.dev/language/mixins](https://dart.dev/language/mixins)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## Mixin目的和使用方法（with）
<b>官网文档：</b>Mixins are a way of defining code that can be reused in multiple class hierarchies. They are intended to provide member implementations en masse.

<b>大概意思：</b>Mixin是一种定义可在多个类层次结构中复用代码的方法。Mixin的目标是为这些类提供一批成员实现（类属性+类方法）。

<b>总结起来：</b>使用Mixin可以让代码被其他类所使用（包括属性和方法）。

<b>使用方法：</b>通过`mixin`关键字定义一个Mixin类；通过`with`关键字，一个类可以同时复用多个mixin成员实现。

<b>代码样例：</b>如下代码，ClassA同时拥有了MixinOne+MixinTwo+MixinThree这3个Mixin的所有成员属性和类方法（感觉有的像多继承？）。

```dart
mixin MixinOne {
  ......
}

mixin MixinTwo {
  ......
}

mixin MixinThree {
  ......
}

class ClassA extends SupperClass with MixinOne, MixinTwo, MixinThree {
  ......
}
```

Mixin的使用有哪些约束呢？
1. Mixin<b>不能继承</b>其他Mixin或者抽象类。
2. Mixin<b>不能有构造方法</b>，也就是Mixin不能被实例化。

## Mixin限定/继承其他类型（on）
为了更好的维护Mixin这些可复用的代码，我们有时需要严格限定使用Mixin的类型，通过`on`关键字达到目的。

<b>代码样例：</b>如下代码，MixinFine通过`on`关键字限定使用它的类型是SupperClass，凡是使用MixinFine的类，必须extends继承SupperClass这个Mixin限定的类型。

```dart
class SupperClass {
  ......
}

mixin MixinFine on SupperClass {
  ......
}

class ClassFine extends SupperClass with MixinFine {
  ......
}
```

## mixin class介绍和使用（类+Mixin）
我们通过`mixin`定义一个Mixin，通过`class`定义一个类；那么通过`mixin class`就可以定义一个mixin和一个类，它们具有<b>相同的名字</b>和<b>相同的类型</b>。

Mixin和类的所有约束，在mixin class同时生效，包括如下：
1. Mixin不支持extends继承其他类和with复用其他Mixin，因此mixin class也不能有extends继承其他类和with复用其他Mixin
2. 普通类不支持通过on关键字限定可使用类型，因此mixin class也不支持on关键字（但是我们可以通过abstract达到此目的）。

<b>代码样例：</b>如下代码，`mixin class`可通过`with`关键字当成Mixin被使用，也可通过`extends`关键字当成类被继承使用。

```dart
abstract mixin class Musician {
  // 含有abstract方法，使用它的类必须实现本方法
  void playInstrument(String instrumentName);

  void playPiano() {
    playInstrument('Piano');
  }
  
  void playFlute() {
    playInstrument('Flute');
  }
}

class Virtuoso with Musician {
  // with关键字，Musician作为一个Mixin被使用
  void playInstrument(String instrumentName) {
    print('Plays the $instrumentName beautifully');
  }  
} 

class Novice extends Musician {
  // extends关键字，Musician作为一个类被继承
  void playInstrument(String instrumentName) {
    print('Plays the $instrumentName poorly');
  }  
}
```

## 扩展问题：Mixin如何解决二义性？（覆盖）
通过上面的说明，一个类可以使用多个Mixin的实现，那么有个问题：<b>他们是如何解决二义性的呢？</b>

<b>样例说明：</b>如下代码，我们有2个Mixin，他们的属性和方法都是相同，同时使用他们时，最终的属性和方法是哪个Mixin的呢？

```dart
mixin MixinA {
  String className = "MixinA";

  void log() {
  print(className);
  }
}

mixin MixinB {
  String className = "MixinB";

  void log() {
  print(className);
  }
}

class ClassMixinAB with MixinA, MixinB {
}

class ClassMixinBA with MixinB, MixinA {
}

void main() {
  ClassMixinAB mixinAB = ClassMixinAB();
  mixinAB.log();
  // 结果：MixinB

  ClassMixinBA mixinBA = ClassMixinBA();
  mixinBA.log();
  // 结果：MixinA
}
```

通过上面2个代码样例，基本可以判断：Mixin解决二义性的方式非常粗暴，后面Mixin<b>覆盖</b>前面Mixin！！！

## Mixin使用场景：打印State<T>生命周期日志
Mixin通过代码复用，可以应用在很多的应用场景。下面代码样例，可以在Flutter组件生命周期逻辑执行之后，打印响应的日志。

<b>样例代码：</b><b>LogStateMixin</b>通过`on`关键字限定/继承了State类型，内部的方法，均通过super代理了State的内容，同时打印相应的日志。

```dart
mixin LogStateMixin<T extends StatefulWidget> on State<T> {

  @override
  void initState() {
    super.initState();
   print("====initState====");
  }
  
  @override
  void dispose() {
   super.dispose();
   print("====dispose====");
  }

  // 其他方法......
}
```

在凡是想要监听组件的生命周期的组件中，可以使用上面的Mixin即可，无其他侵入代码，特别适合在Flutter应用研发过程中，通过日志观测组件的生命周期：

```dart
class _MinePageState extends State<MinePage> with LogStateMixin<MinePage>
  // 我的页面逻辑，无需关心日志....
}
```

这样在页面初始化、销毁的时候，打印响应的日志。

---
我的本博客原地址：[https://ntopic.cn/p/2023093001](https://ntopic.cn/p/2023093001/)

---
