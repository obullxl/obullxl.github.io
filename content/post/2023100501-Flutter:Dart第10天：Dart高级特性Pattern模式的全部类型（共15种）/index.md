+++
slug = "2023100501"
date = "2023-10-05"
lastmod = "2023-10-05"
title = "Flutter/Dart第10天：Dart高级特性Pattern模式的全部类型（共15种）"
description = "Pattern模式是Dart 3.0发布的3个高级特性之一，在第09天我们学习了模式的概览和用法，对模式的强大之处有了基本的认识，今天我们来看看Dart中的全部模式类型，总共有15种，它们包括逻辑或、逻辑与、关系、值转换、空检测、空断言、常量、变量、标识符、括号、List列表、Map映射、Record记录、对象和通配符……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "模式" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/pattern-types](https://dart.dev/language/pattern-types)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

和操作符一样，模式运算也遵循一定的<b>优先级</b>规则，我们可以通过增加括号`()`让低优先级规则的模式优先运算：
1. 逻辑或模式低于逻辑与模式，逻辑与模式低于关系模式：`逻辑或 < 逻辑与 < 关系`。
2. 一元模式优先级相同：值转换、空检测、空断言。
3. 其他的模式都具有最高的优先级，集合类型（List列表、Map映射和Record记录）和对象模式包含了其他数据，因此作为外部模式优先运算。

## 逻辑或模式（Logical-or）
<b>模式语法：</b>`子模式1 || 子模式2`

<b>模式规则：</b>逻辑或模式通过`||`分割子模式，从左到右，任何一个子模式匹配则本模式匹配，且后面的子模式不在运算。

子模式可以绑定变量，但是每个子模式绑定的变量<b>必须相同</b>，因为任一子模式匹配则后面的子模式不在运算。

```dart
var isPrimary = switch (color) {
    Color.red || Color.yellow || Color.blue => true,
    _ => false
};
```

## 逻辑与模式（Logical-and）
<b>模式语法：</b>`子模式1 && 子模式2`

<b>模式规则：</b>逻辑与模式通过`&&`分隔子模式，从左到右，任何一个子模式未匹配则本模式未匹配，且后面的子模式不在运算。

子模式可以绑定变量，且每个子模式绑定的变量<b>不能重叠</b>，因为本模式匹配代表每个子模式都必须匹配运算，如果重叠则意味着变量被赋值多次。

```dart
switch ((1, 2)) {
  case (var a, var b) && (var c, var d): // ...
}
```

## 关系模式（Relational）
<b>模式规则：</b>关系模式通过和给定的<b>常量</b>进行比较完成匹配（比较操作符：`==`，`!=`，`<`，`>`，`<=`，`>=`），`true`代表匹配成功。通常情况下，关系模式和逻辑与模式配合使用。

```dart
String asciiCharType(int char) {
  const space = 32;
  const zero = 48;
  const nine = 57;

  return switch (char) {
    < space => 'control',
    == space => 'space',
    > space && < zero => 'punctuation',
    >= zero && <= nine => 'digit',
    _ => ''
  };
}
```

## 值转换模式（cast）
<b>模式语法：</b>`变量 as 类型`，如：`foo as String`

<b>模式规则：</b>值转换模式允许在对象数据解构过程中进行类型转换，如果类型无法转换，则会产生错误，建议在类型转换之前，进行类型断言。

```dart
(num, Object) record = (1, 's');
var (i as int, s as String) = record;
```

## 空检测模式（Null-check）
<b>模式语法：</b>`子模式?`

<b>模式规则：</b>如果检测的值不为NULL，则模式匹配。它允许绑定一个变量，变量的类型是该不可为NULL值类型基类。

```dart
String? maybeString = 'nullable with base type String';
switch (maybeString) {
  case var s?:
  // 's' has type non-nullable String here.
}
```

## 空断言模式（Null-assert）
<b>模式语法：</b>`子模式!`

<b>模式规则：</b>首先检测对象不为NULL，然后检测对象数据值。如果匹配的值为NULL，则会抛出错误。它常用于解构并赋值场景，且保证所赋值非NULL。

```dart
List<String?> row = ['user', null];
switch (row) {
  case ['user', var name!]: // ...
  // 'name' is a non-nullable string here.
}

(int?, int?) position = (2, 3);
var (x!, y!) = position;
```

## 常量模式（constant）
当值为常量时，常量模式匹配，常量包括：`123`, `null`, `string`, `math.pi`, `SomeClass.constant`, `const Thing(1, 2)`, `const (1 + 2)`等。

```dart
switch (number) {
  // Matches if 1 == number.
  case 1: // ...
}
```

我们可以通过字面常量、命名的常量等方式使用常量模式：
 - 数字字面量：`123`, `45.56`
 - 布尔字面量：`true`
 - 字符串字面量：`string`
 - 命名常量：`someConstant`, `math.pi`, `double.infinity`
 - 常量构造器：`const Point(0, 0)`
 - 常量集合字面量：`const []`, `const {1, 2}`

其他更多复杂的常量表达式，可以通过`()`包裹，并增加`const`前缀：`const (const (1 + 2))`

```dart
// List or map pattern:
case [a, b]: // ...

// List or map literal:
case const [a, b]: // ...
```

## 变量模式（variable）
模式规则：变量模式一般在解构和赋值中，它匹配模式、解构对象并完成赋值，如：`var bar`, `String str`, `final int _`。变量的作用域为模式所在的作用域。如果变量指定了<b>类型</b>，那么当对象类型和值均匹配时，模式才被匹配。通配符模式是一个特殊的变量模式。

```dart
switch ((1, 2)) {
  // 'var a'和'var b'是变量模式，它们值分别为`1`和`2`
  case (var a, var b): // ...
  // 'a'和'b'的作用域在case代码块
}

switch ((1, 2)) {
  // `2`是数字类型，与'String b'不匹配，因此本模式为匹配
  case (int a, String b): // ...
}
```

## 标识符模式（identifier）
标识符模式与常量模式或变量模式类似：
 - 变量申明上下文：给变量申明一个标识符，如：`var (a, b) = (1, 2);`
 - 变量赋值上下文：给已经存在的标识符赋值，如：`(a, b) = (3, 4);`
 - 模式匹配上下文：当作一个命名常量模式（除名字是`_`外）
 - 任意上下文中的通配符标识符：能匹配任何值且忽略该值，如：`case [_, var y, _]: print('The middle element is $y');`

## 括号模式
<b>模式语法：</b>`(子模式)`

<b>代码样例：</b>如下代码，和表达式一样，增加括号`()`目的是提高模式的优先级。

```dart
final (x, y, z) = (true, true, false);

// 没有括号：true
x || y && z => 'matches true',

// 增加括号：false
(x || y) && z => 'matches false',
```

## 列表模式（List）
<b>模式语法：</b>`[子模式1, 子模式2]`

List列表模式首先匹配`List`类型，然后匹配列表元素值，并进行解构和赋值。List列表模式<b>必须</b>匹配整个列表模式元素，我们可以使用`...`占位符匹配剩余的列表元素。

```dart
// 全列表元素匹配
const a = 'a';
const b = 'b';
switch (obj) {
  case [a, b]:
    print('$a, $b');
}

// 占位符匹配剩余元素，且忽略
var [a, b, ..., c, d] = [1, 2, 3, 4, 5, 6, 7];
print('$a $b $c $d'); // 1 2 6 7

// 占位符当作一个子列表
var [a, b, ...rest, c, d] = [1, 2, 3, 4, 5, 6, 7];
print('$a $b $rest $c $d'); // 1 2 [3, 4, 5] 6 7
```

## Map映射模式
<b>模式语法：</b>`{"key": subpattern1, someConst: subpattern2}`

Map映射模式首先匹配`Map`类型，然后匹配元素内容，并进行解构和赋值。Map映射模式<b>不需要</b>匹配所有元素，忽略未被匹配到的元素。

## Record记录模式
<b>模式语法：</b>`(subpattern1, subpattern2)`或者`(x: subpattern1, y: subpattern2)`

Record记录模式首先匹配记录，然后解构其字段。字段数量、类型和值未匹配，则模式匹配失败。Record记录模式必须匹配所有字段。字段getter可由变量和标识符模式推导得到。

```dart
var (myString: foo, myNumber: bar) = (myString: 'string', myNumber: 1);

// Record pattern with variable subpatterns:
var (untyped: untyped, typed: int typed) = record;
var (:untyped, :int typed) = record;

switch (record) {
  case (untyped: var untyped, typed: int typed): // ...
  case (:var untyped, :int typed): // ...
}

// Record pattern wih null-check and null-assert subpatterns:
switch (record) {
  case (checked: var checked?, asserted: var asserted!): // ...
  case (:var checked?, :var asserted!): // ...
}

// Record pattern wih cast subpattern:
var (untyped: untyped as int, typed: typed as String) = record;
var (:untyped as int, :typed as String) = record;
```

## Object对象模式
<b>模式语法：</b>`SomeClass(x: subpattern1, y: subpattern2)`

对象模式首先对象类型和属性类型，并完成对象属性解构，调用getter方法完成赋值。如果类型不一致，则匹配失败。对象的属性名可以忽略，它可以通过变量模式和标识符模式进行推导。和Map映射模式一样，对象模式<b>不需要</b>匹配所有属性，忽略未被匹配到的属性。

```dart
switch (shape) {
  // Matches if shape is of type Rect, and then against the properties of Rect.
  case Rect(width: var w, height: var h): // ...
}

// Binds new variables x and y to the values of Point's x and y properties.
var Point(:x, :y) = Point(1, 2);
```

## 通配符模式
<b>模式语法：</b>`_`
`_`就是通配符模式，它既是变量模式也是标识符模式，但是它无变量也不赋值。它通常作为一个<b>占位符</b>，目的是匹配解构剩下的位置值。通配符如果带有类型，那么它仅仅进行<b>类型检测</b>，而忽略变量和赋值。

```dart
// 占位符
var list = [1, 2, 3];
var [_, two, _] = list;

// 类型检测
switch (record) {
  case (int _, String _):
    print('First field is int and second is String.');
}
```

---
我的本博客原地址：[https://ntopic.cn/p/2023100501](https://ntopic.cn/p/2023100501/)

---
