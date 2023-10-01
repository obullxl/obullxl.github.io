+++
slug = "2023100101"
date = "2023-10-01"
lastmod = "2023-10-01"
title = "Flutter/Dart第06天：Dart基础语法详解（变量）"
description = "前面几天的学习，我们了解了Dart语言的特性（基础语法概览、迭代集合、异步编程和Mixin高级特性）。今天我们深入学习Dart的变量，包括：空安全（Null safety）、变量默认值、延迟变量（late）、final变量和const常量……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "空安全" ]
categories = [ "专业技术" ]
+++

Dart官网文档：[https://dart.dev/language/variables](https://dart.dev/language/variables)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## Dart中的变量
变量是一个对象的引用，引用名就是变量的名称；就算引用是`null`的变量也一样。

变量有3种定义方式：`var`关键字，显示`类型`和`Object`/`dynamic`类型。

```dart
var varName = 'Tom';
String strName = 'Tom';
Object objName = 'Tom';
dynamic dynName = 'Tom';
```

<b>最佳实战：</b>对于局部变量，优先使用`var`关键字，其次为显示`类型`，再次为`Object`，不推荐使用`dynamic`（原因：容易引发运行时错误，后续的学习会讲到）。

当使用`var`关键字定义的变量，Dart会根据值内容，推导出变量的实际类型，如上诉代码`varName`变量最终为`String`类型。

## Null safety空安全
Dart语言强制健壮空安全。空安全可以防止意外使用`null`而导致的错误（还记得在Java编程中，在很多对象使用的地方，我们都需要进行`null`判断，以防止NPE的发生）。Dart在编译期间，就会进行null潜在错误检测分析，从而防止这些错误的发生（注意：并不是所有场景都能检测分析到，后面章节会讲到）。

<b>代码样例：</b>如下代码，变量`strName`的默认值为`null`，在其他编程语言如Java语言中，下面的代码是合法的，但是在运行时，当执行`strName.length`时会引发NPE异常；但是在Dart语言中，以下代码是<b>非法</b>的（无法编译），因此阻止发生NPE等这些潜在的错误。

```dart
String strName;
print(strName.length)
```

Dart为了强制执行空安全，有3个<b>关键改变</b>：
1. 如果明确希望某个变量、参数或者其他组件是可以为`null`的，那么需要在类型后面增加一个`?`标识：
```dart
String? name  // `name`的值可能为`null`, 或者为某个字符串
String name   // `name`的值只能是某个字符串，不能为`null`
```
2. Dart的变量在使用之前，必须被<b>初始化</b>。可空变量（含有`?`标识）的默认值为`null`，即默认初始化为null，因此无需显示的赋值初始化；而非空变量因没有默认值，因此必须显示<b>赋值</b>初始化。
3. 禁止直接访问可空类型的属性或者表达式方法，包括访问`null`对象的`hashCode`和`toString()`方法（记住：Dart中一起皆对象，null也是），也会引发错误。

Dart空安全通过以上3个<b>关键改变</b>，保证`null`潜在错误在代码编写阶段就能被前置发现，而不是等到代码运行时。

## 变量默认值
第2章节中，其实已经提到一点：任何变量在使用之前，必须被初始化；可空变量因为默认值为null，因此可无需显示初始化；非空变量必须显示初始化。

<b>特别注意：</b>非空变量只需要确保它在被使用时已经初始化即可，而不是必须在申明的时候。如下代码，变量`lineCount`在申明时并未初始化，但是在`print`被使用时，Dart语言检测到它已经被初始化了，因此如下代码是空安全有效代码。

```dart
// 申明：未被初始化
int lineCount;

if (weLikeToCount) {
  lineCount = countLines();
} else {
  lineCount = 0;
}

// 使用：Dart检测到已经被初始化，因此是可以使用
print(lineCount);
```

## late延迟变量
顶级变量和类变量会延迟初始化，当第一次使用到这些变量时，初始化代码的逻辑才会被执行（即：延迟初始化）。

在大多数情况下，Dart可以检测并分析一个非空变量在使用时已经初始化，但是在有些场景下，Dart无法检测分析或者检测分析会失效。最常见的2种场景：<b>顶级变量</b>和<b>实例变量</b>，Dart无法确定它们在被使用时是否已经被初始化了。

如果我们明确一个非空变量在被使用之前能完成初始化（但Dart却无法检测到），可通过增加`late`关键字，告诉Dart该变量为延迟变量，在被使用之前确保能被初始化。当然，对于`late`延迟变量，在被使用时却并没有初始化，那么使用它同样会导致运行时<b>错误</b>。

```dart
late String description;

void main() {
  description = 'Feijoada!';
  print(description);
}
```

`late`延迟变量主要处理以下2种场景：
1. 这些变量并不是必须的，同时初始化它们非常耗时或者浪费资源（如网络交互等）。
2. 在初始化实例变量时，需要用到实例本身。

<b>代码样例：</b>如下代码，当变量`temperature`在第1次被使用时，才会被调用`readThermometer()`方法，即该方法仅仅被调用1次。

```dart
late String temperature = readThermometer();
```

## final变量和const常量
<b>最佳实践：</b>如果我们明确一个变量在被初始化之后，它的引用值再也不会变化，那么使用`final`或者`const`修饰，而不是使用`var`者显示`类型`。
1. `const`变量隐式为`final`变量
2. `const`变量是编译期的常量变量
3. 实例变量可以是`final`变量但不能是`const`变量（原因：实例在运行时才能确定，因此其变量无法作为编译期常量）

```dart
final name = 'Bob';
final String nickname = 'Bobby';
const bar = 1000000;
const double atm = 1.01325 * bar;
```

<b>特别注意：</b>`const`不仅可以申明如上代码的常量变量，它也可以用于申明创建常量值，也可以用于申明创建常量值的构造器（还记得第2天学习内容：const构造函数？）；同时，任何变量，都可以有常量值。

```dart
// 1. 常量值
var foo = const [];
final bar = const [];
const baz = []; // 等同：`const []`

// 2. 非final/const变量，它的常量值可以更新
foo = const [1, 2, 3];
```

<b>特别注意：</b>常量的定义，可以包含类型检测、类型转换、集合过滤和集合展开操作符。

```dart
// `i`是一个`Object`类型常量，它的值是int数字值
const Object i = 3;

// 1. 类型转换
const list = [i as int];

// 2. 类型检测和集合过滤
const map = {if (i is int) i: 'int'};

// 3. 类型检测、集合过滤、集合展开
const set = {if (list is List<int>) ...list};
```

<b>final变量</b>和<b>const常量</b>总结：
1. `final`变量不可修改，但是它的值是<b>可以更新</b>。
2. `const`常量不可修改，同时它的值也<b>不可以更新<b>，即它的值是不可变的（<b>immutable</b>）。

```dart
void main() {
  // 1. final变量
  final finalList = [1, 2, 3];
  print('1.1 final变量: $finalList');

  // 输出：1.1 final变量: [1, 2, 3]

  finalList
    ..add(4)
    ..add(5);
  print('1.2 final变量: $finalList');

  // 输出：1.2 final变量: [1, 2, 3, 4, 5]

  // 2. const常量
  const constList = ['a', 'b', 'c'];
  print('2.1 const常量: $constList');

  // 输出：2.1 const常量: [a, b, c]

  constList
    ..add('c')
    ..add('d');
  print('2.2 const常量: $constList');

  // 错误：
  // Unhandled exception:
  // Unsupported operation: Cannot add to an unmodifiable list
  // #0      UnmodifiableListMixin.add (dart:_internal/list.dart:114:5)
}
```

---
我的本博客原地址：[https://ntopic.cn/p/2023100101](https://ntopic.cn/p/2023100101/)

---
