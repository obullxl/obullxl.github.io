+++
slug = "2023100401"
date = "2023-10-04"
lastmod = "2023-10-04"
title = "Flutter/Dart第09天：Dart高级特殊Pattern模式的概览和用法"
description = "Dart 3.0在语法层面共发布了3个高级特性，第一个特性Record记录我们在前面已经学习和探究。今天我们来学习第二个高级类型Pattern模式，由于内容较多，共分2篇文章进行介绍，本文首先介绍模式的概览和用法，包括匹配、解构、在变量申明、赋值、循环、表达式等应用场景……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "模式" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/patterns](https://dart.dev/language/patterns)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## Pattern模式匹配的定义
<b>官网定义：</b>Patterns are a syntactic category in the Dart language, like statements and expressions. A pattern represents the shape of a set of values that it may match against actual values.

初看定义不太好理解，感觉有点绕，<b>大概意思：</b>模式是Dart语言的一种语法分类，就像声明和表达式一样。模式代表了一组实际值的形状，这个形状可以匹配到实际值。（<b>特别注意：</b>这里的Pattern和正则表达式没有任何关系！）

有几个重要的概念：语法、形状、匹配
1. 语法：语法是一个编码语言的基础，可见模式在Dart中的重要程度。
2. 形状：或者说结构，就是一组实际值是如何组织在一起的一种抽象（结构定义）。
3. 匹配：根据一组值的形状，我们匹配到对应的值。

举一个List列表的例子，可能不是完全恰当，但是可以<b>帮忙</b>我们理解模式的这段定义：
1. 语法：`final aList = [1, 2, 3];`这个是定义列表的语句，其中`aList`代表变量名，列表采用`[]`包裹，元素采用`,`分隔，最后`;`结束等等，这些都是Dart中的语法。
2. 形状：列表采用`[]`包裹，元素采用`,`分隔，元素类型`int`由Dart自动推导出来，这些都是这一组值的形状，就是长什么样。
3. 匹配：`aList[0] == 1`根据列表的语法和形状，可以匹配到实际值。

## Pattern模式的用途
Pattern模式主要作用：<b>匹配</b>值、<b>解构</b>值。匹配和解构可以同时作用，需要根据上下文和值的形状或结构具体来看。

首先，模式可以让我们确定某个值的一些信息，包括：
1. 有一个明确的形状（或者结构）。
2. 是一个明确的常量。
3. 它和某个值相等（即可用于比较）。
4. 有一个明确的类型。

然后，模式解构可以用一种便利的<b>语法</b>，把这个值进行<b>分解</b>，还可以绑定到某个变量上面。

### 匹配
匹配就是校验某个值是否符合我们预期，换句话说，我们是在检测某个值是否符合某种结构且它的值与指定值相等。

我们在编码过程中，很多逻辑其实都是在进行模式，举例如下：

```dart
// 常数匹配：1 == number ？
switch (number) {
  case 1:
    print('one');
}

// 列表匹配：`obj`是一个2个元素列表
// 元素匹配：`obj`的2个元素值分别为`a`和`b`
const a = 'a';
const b = 'b';
switch (obj) {
  case [a, b]:
    print('$a, $b');
}
```

### 解构
当一个对象和一个模式相匹配，那么这个模式可以访问对象的数据，并可以把这个对象<b>拆分</b>成不同部分。换句话说，这个模式<b>解构</b>了这个对象。

<b>代码样例：</b>如下代码，List列表解构，和解构模式中的嵌套匹配模式。

```dart
// 列表解构：`[a, b, c]`结构`numList`对象
// 1. 匹配：`[a, b, c]`代表了具有3个元素的列表
// 2. 拆分：列表的3个元素，分别赋值给了新的变量`a`、`b`和`cs`
var numList = [1, 2, 3];
var [a, b, c] = numList;
print(a + b + c);

// 列表模式：包含2个元素，且第1个元素是`a`或`b`，第2个元素赋值给变量`c`
switch (list) {
  case ['a' || 'b', var c]:
    print(c);
}
```

## 模式的应用场景
在Dart语言总，有几个常见可以使用模式：
1. 局部变量的<b>申明</b>和<b>赋值</b>。
2. `for`和`for-in`循环语句。
3. `if-case`和`switch-case`语句。
4. 集合相关的<b>控制流</b>。

### 变量申明
我们可以在Dart允许本地变量声明的任何地方使用模式变量声明，模式变量申明必须由`var`或者`final` + <b>模式</b>组成（这也是Dart的模式变量的语法）。

<b>代码样例：</b>如下代码，使用模式，我们申明了`a`，`b`和`c`三个变量（并且完成赋值）。

```dart
var (a, [b, c]) = ('str', [1, 2]);
```

### 变量赋值
上小节<b>变量申明</b>的代码样例中，其实已经进行了模式变量赋值：首先进行模式匹配，然后解构对象，最终进行遍历赋值。

<b>代码样例：</b>如下代码，采用变量赋值模式，轻松进行了2个元素值交换，而无需使用第3个变量。

```dart
var (a, b) = ('left', 'right');
(b, a) = (a, b);
print('$a $b');
```

### Switch和表达式模式
本文开头的样例其实已经提到，任何`case`的语句其实都包含了一个模式。在`case`中，可以应用任何的模式，变量赋值的<b>作用域</b>仅在Case语句内部。

Case模式可以匹配失败，它允许控制流：
1. 匹配并解构`switch`对象。
2. 匹配失败，则继续执行匹配。

```dart
switch (obj) {
  // 匹配：1 == obj
  case 1:
    print('one');

  // 匹配：[first, last]区间
  case >= first && <= last:
    print('in range');

  // 匹配：Record记录，包含2个字段
  // 赋值：`a`和`b`局部变量（作用域：本Case内部）
  case (var a, var b):
    print('a = $a, b = $b');

  default:
}

// 逻辑或模式：多个case共用
var isPrimary = switch (color) {
  Color.red || Color.yellow || Color.blue => true,
  _ => false
};

switch (shape) {
  case Square(size: var s) || Circle(size: var s) when s > 0:
    print('Non-empty symmetric shape');
}
```

### for和for-in循环模式
主要作用：迭代和解构集合。

<b>代码样例：</b>如下代码，`for`循环匹配模式，并解构和赋值给变量。

```dart
Map<String, int> hist = {
  'a': 23,
  'b': 100,
};

// 匹配：`MapEntry`类型，继续匹配`key`和`value`命名字段子模式
// 赋值：调用`key`和`value`的`getter`并赋值给`key`和`value`变量
for (var MapEntry(key: key, value: count) in hist.entries) {
  print('$key occurred $count times');
}

// 上诉代码的简写
for (var MapEntry(:key, value: count) in hist.entries) {
  print('$key occurred $count times');
}
```

## 其他场景模式
本文前面的章节，我们主要是展示Dart类型模式和解构，当然也包括`(a, b)`内容交换的例子。本章进一步学习其他的场景模式。

通过本章学习，主要解决我们几个问题：
1. 什么时候我们需要用到模式，我们为什么需要模式？
2. 模式主要解决什么类型的问题？
3. 什么样的模式最适合？

### 解构多个返回值
在之前的学习中，<b>Record记录</b>的用途之一就是聚合多个值，并让函数返回多个值。模式能匹配并解构Record记录，并赋值给局部变量。

<b>代码样例：</b>如下代码，`userInfo(json)`返回一个`位置字段`的记录，被解构并把位置值赋值给了`name`和`age`局部变量。

```dart
// Record记录的使用
var info = userInfo(json);
var name = info.$1;
var age = info.$2;

// Record解构和赋值
var (name, age) = userInfo(json);
```

### 解构类实例
对象模式能匹配<b>命名</b>的对象类型，可以解构对象的数据，并调用对象属性的`getters`方法进行赋值。

<b>代码样例：</b>如下代码，<b>命名</b>类型`Foo`实例`myFoo`被解构并进行赋值给`one`和`two`变量。

```dart
final Foo myFoo = Foo(one: 'one', two: 2);
var Foo(:one, :two) = myFoo;
print('one $one, two $two');
```

### 代数数据类型
对象解构和Switch模式有助于编写代数数据类型风格代码，它比较适合以下几种场景：
1. 有一群相关联的类型。
2. 每个类型都有一个相同的操作，但这个操作对每个类型而言又有差异。
3. 我们希望把这个操作能一把实现，而不是把实现散落在每个类型中。

<b>样例代码：</b>如下代码，`Shape`是一个父类，2个或更多的子类都有<b>计算面积</b>的方法，最终通过`calculateArea()`函数一把实现了。

```dart
sealed class Shape {}

class Square implements Shape {
  final double length;
  Square(this.length);
}

class Circle implements Shape {
  final double radius;
  Circle(this.radius);
}

double calculateArea(Shape shape) => switch (shape) {
      Square(length: var l) => l * l,
      Circle(radius: var r) => math.pi * r * r
    };
```

### 校验JSON格式
前面章节，我们学习了`List`和`Map`类型的匹配和解构，它们也适用于JSON的`key-value`键值对。

<b>代码样例：</b>如下代码，在已知JSON格式的情况下，我们可以通过List和Map完成JSON的解构和赋值。

```dart
var json = {
  'user': ['Lily', 13]
};

var {'user': [name, age]} = json;
```

但是，当JSON格式不明确的情况下，我们可以通过解构来校验JSON的格式。

<b>代码样例：</b>如下代码，我们通过<b>case模式</b>，完成了JSON数据的校验和赋值。

```dart
if (json case {'user': [String name, int age]}) {
  print('User $name is $age years old.');
}
```

如上代码，Case模式的匹配和赋值操作如下：
1. `json`是一个非空的`map`，进一步匹配<b>map模式</b>。
2. `json`包含一个名为`user`的属性，且它是一个包含2个元素的`list`类型，list中2个元素类型分别为`String`和`int`。
3. 最终，list的2个元素分别赋值给了`name`和`age`局部变量。

---
我的本博客原地址：[https://ntopic.cn/p/2023100401](https://ntopic.cn/p/2023100401/)

---
