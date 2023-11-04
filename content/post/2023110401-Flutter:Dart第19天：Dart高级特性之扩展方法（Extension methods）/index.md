+++
slug = "2023110401"
date = "2023-11-04"
lastmod = "2023-11-04"
title = "Flutter/Dart第19天：Dart高级特性之扩展方法（Extension methods）"
description = "扩展方法（Extension methods）是Dart语言的另一个高级的特性，我们可以在不改变、不继承原类型或类情况下，给类型或类增加方法，增强其功能。在Java中，我们经常看到StringUtils/MapUtils等String/Map类型的工具类，但是在Dart中，这些工具类统统不需要，因为可以直接给String/Map类增加扩展方法（Extension methods）达到同等目的……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "类" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/extension-methods](https://dart.dev/language/extension-methods)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

## 扩展方法概述
当我们使用了一些被广泛使用的其他库或者自己的库时，我们不太可能去修改这个库API，但是我们又想给库增加一些方法，该怎么办？如：我们想给`String`类增加一些我自己常用的方法。

Dart作为一门集百家之长的编程语言，也考虑到了这个需求点，它提供了一个<b>扩展方法</b>（Extension methods）来解决问题问题。

如下代码样例，`String`类型转换`int`数字类型，常规的做法如下：

```dart
int.parse('123');
```

那如果`String`类型提供一个转为`int`数字类型的方法，是不是更好：

```dart
'123'.parseInt()
```

想要实现上诉目的，通过<b>扩展</b>`String`类型，提供对应方法即可：

```dart
import './19-ntopic-string-apis.dart';

void main() {
  print('123'.parseInt());
  // 结果：123
}

//
// 19-ntopic-string-apis.dart 内容
//
extension NumberParsing on String {
  int parseInt() {
    return int.parse(this);
  }
}
```

## 使用扩展方法
上一章节的最后，其实我们已经展示了如何定义和使用扩展方法。使用扩展方法，和使用类型的其他方法没有任何差异。

接下来我们来看看，扩展方法在静态类型和动态类型的使用，和如何解决同名扩展方法冲突。

### 静态类型和dynamic动态类型
<b>特别注意：</b>`dynamic`动态类型禁止使用扩展方法！如下代码样例，会抛出`NoSuchMethodError`运行时异常。

```dart
import './19-ntopic-string-apis.dart';

void main() {
  print('123'.parseInt());
  // 结果：123

  dynamic d = '2';
  // NoSuchMethodError: Class 'String' has no instance method 'parseInt'.
  print(d.parseInt());
}
```

但是扩展方法可用于<b>类型推导</b>上，如下代码无任何问题，因为变量`v`的类型推导成`String`类型：

```dart
import './19-ntopic-string-apis.dart';

void main() {
  print('123'.parseInt());
  // 结果：123

  var v = '2';
  print(v.parseInt());
  // 结果：2

  dynamic d = '2';
  // NoSuchMethodError: Class 'String' has no instance method 'parseInt'.
  print(d.parseInt());
}
```

`dynamic`动态类型不可用户扩展方法的原因是，扩展方法只能接收静态类型，因此调用扩展方法和调用静态方法一样高效。

### 扩展方法冲突
我们在应用中，会引入多个库，如果有多个库都对同类型增加了同名的拓展方法，那么就导出扩展方法冲突了。如：对`String`类型，库A和库B都有`pareInt()`扩展方法，那么这个扩展方法就存在冲突。

一般情况下，有3种方法来解决扩展方法的冲突：

第一种方法，在引入库时，通过`show`或者`hide`关键字限制扩展方法：

```dart
// String扩展方法：parseInt()
import 'string_apis.dart';

// String扩展方法：parseInt(), `hide`隐藏扩展类型
import 'string_apis_2.dart' hide NumberParsing2;

// ···
// 使用了 'string_apis.dart' 中定义的扩展方法：parseInt()
print('42'.parseInt());
```

第二种方法，显示指定<b>扩展类型</b>的扩展方法：

```dart
// 扩展类型：NumberParsing，扩展方法：parseInt()
import 'string_apis.dart';

// 扩展类型：NumberParsing2，扩展方法：parseInt()
import 'string_apis_2.dart';

// 显示使用扩展类型
print(NumberParsing('42').parseInt());
print(NumberParsing2('42').parseInt());
```

第三种方法，假设第二种方法的<b>扩展类型</b>也一样，那么可在引入库增加前缀解决：

```dart
// 扩展类型：NumberParsing，扩展方法：parseInt()
import 'string_apis.dart';

// 扩展类型：NumberParsing，扩展方法：parseInt()，parseNum()
import 'string_apis_3.dart' as rad;

// 'string_apis.dart' 扩展方法：parseInt()
print(NumberParsing('42').parseInt());

// 'string_apis_3.dart' 扩展方法：parseInt()
print(rad.NumberParsing('42').parseInt());

// 'string_apis_3.dart' 扩展方法：parseNum()
print('42'.parseNum());
```

因为`parseNum()`扩展方法不存在冲突，因此可直接使用。仅当存在扩展类型冲突时，才需要增加前缀。

## 实现扩展方法
在前面2个章节，其实已经提到了部分扩展方法的实现方法。扩展方法实现语法如下（扩展类型名是可选的）：

```dart
extension <extension name>? on <type> {
  (<member definition>)*
}
```

如下代码样例，对`String`类型，增加了2个扩展方法（扩展类型名：`NumberParsing`）：

```dart
extension NumberParsing on String {
  int parseInt() {
    return int.parse(this);
  }

  double parseDouble() {
    return double.parse(this);
  }
}
```

扩展类型中的成员，可以是方法、Getters、Setters和操作符，同时也可以是静态属性和静态方法，外围可通用普通类型静态属性和静态方法一样使用。

### 未命名的扩展类型
我们定义未命名的扩展，它们的可见范围仅在库内容（类似于私有属性和方法）；由于扩展类型未命名，因此无法明确的用于冲突解决，它们的静态属性和静态方法，也只能在扩展内部使用：

```dart
extension on String {
  bool get isBlank => trim().isEmpty;
}
```

## 实现泛型扩展
扩展也运用在泛型参数，如下代码样例，对`List<T>`增加扩展方法和操作符，类型T在调用时才绑定静态类型：

```dart
extension MyFancyList<T> on List<T> {
  int get doubleLength => length * 2;
  List<T> operator -() => reversed.toList();
  List<List<T>> split(int at) => [sublist(0, at), sublist(at)];
}
```


---
我的本博客原地址：[https://ntopic.cn/p/2023110401](https://ntopic.cn/p/2023110401/)

---
