+++
slug = "2023092401"
date = "2023-09-24"
lastmod = "2023-09-24"
title = "Flutter/Dart第02天：Dart基础语法（建议收藏）"
description = "第1天安装并初体验了一把Dart程序，本文按照Dart官网的“代码实验室”把Dart的基础语法练习一遍，基础语法特性很多，因此建议收藏本博客了 [本博客疑问：为什么函数有了命名参数，还需要可选的位置参数？欢迎评论区讨论！]……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台" ]
categories = [ "专业技术" ]
+++

Dart官网代码实验室：[https://dart.dev/codelabs/dart-cheatsheet](https://dart.dev/codelabs/dart-cheatsheet)

特别说明：为了更进一步验证Dart代码特性，下面示例的代码并非与官方代码完全一致（为了探究细节，默认比官方代码要复杂一些）。

## 字符串插值：${}
基础语法：字符串中，可以通过${}插入上下文中变量和变量运算值。

```dart
void main() {
  // 1. 字符串插值
  var a = 2;
  var b = 3;
  var c = 'Hello';
  print('1. 字符串插值: ${c.toUpperCase()} Dart:a is ${a} and b is ${b}, so a>b is ${a > b}, a+b=${a + b}');
  // 结果：1. 字符串插值: HELLO Dart:a is 2 and b is 3, so a>b is false, a+b=5
}
```

## 变量赋值：?和null
基础语法：Dart是空安全（或者null安全）的语言，也就是说除非显示声明变量是可为null的，否则他们不能为null。默认情况下，变量默认是不能为null的。

```dart
void main() {
  // 2. 变量赋值
  // 非法声明：int d = null; String s;
  int d = 4;
  int? e;
  String f = 'Hello';
  String? g;
  print('2. 变量赋值: d: ${d}, e:${e}, f:${f}, g:${g}');
  // 结果：2. 变量赋值: d: 4, e:null, f:Hello, g:null
}
```

## 空运算符：??和??=
基础语法：用于处理可能会为空值的变量，??判断是否为空，??=当为空时才运行赋值。

特别注意：??和??=这两个运算符中间不能有空格。

```dart
void main() {
  // 3. 空运算符
  int? h;
  h ??= 3;
  h ??= 5; // 不生效，因为此时h非空
  int i = 1 ?? 3; // 1非空，所以3不生效
  int j = null ?? 4;
  print('3. 空运算符: h=${h}, i=${i}, j=${j}');
  // 结果：3. 空运算符: h=3, i=1, j=4
}
```

## 访问空对象属性：.?
基础语法：为了正常访问可能为空对象的属性。在Java中，通过if条件判断来访问属性，如：int a = (obj == null) ? 0 : obj.getA();

```dart
void main() {
  // 4. 访问空对象属性
  String? k;
  String? l = 'Hello';
  var m = k?.toLowerCase()?.toUpperCase();
  var n = l?.toLowerCase()?.toUpperCase();
  print('4. 访问空对象属性: m=${m}, n=${n}');
  // 结果：4. 访问空对象属性: m=null, n=HELLO
}
```

## 集合类型：[]和{}
基础语法：Dart存在内置的基础集合类型，包括list, set和map等，可以指定元素类型。list元素可以重复，set和map不允许重复。

特别注意：这里只是简单用例，集合类型的其他用法，我在下次学习并通过博客分享。

```dart
void main() {
  // 5. 集合类型
  var aList = <String>['a', 'b', 'b'];
  var aSet = <String>{'a', 'b', 'b'};
  var aMap = <String, String>{'a': 'Hello', 'b': 'World', 'b': 'Dart'};
  print('5. 集合类型: aList=${aList}, aSet=${aSet}, aMap=${aMap}');
  // 结果：5. 集合类型: aList=[a, b, b], aSet={a, b}, aMap={a: Hello, b: Dart}
}
```

## 箭头语法函数：=>
基础语法：箭头是定义函数的一种简便方法，箭头右边的执行结果作为返回值。

```dart
void main() {
  // 6. 箭头语法函数
  String joinWithCommas(List<int> values) => values.join(',');
  final cList = <int>[2, 5, 7];
  print('6. 箭头语法函数: cList join=${joinWithCommas(cList)}');
  // 结果：6. 箭头语法函数: cList join=2,5,7
}
```

## 级联和空判断：..和?..
基础语法：为了简便对同一个对象连续执行多个方法，它每次执行均返回的是操作对象引用，而不是操作结果。

特别说明：常规情况下myObject.someMethod()返回的是方法的执行结果，但是级联操作myObject..someMethod()返回的是myObject引用本身，那么它就可以连续执行多个方法，如：myObject..someMethod()..otherMethod()等。

级联联合空判断，可以在连续执行多个方法的时候，无需担心操作对象为null，如以下代码样例：将 BigObject 的 anInt 属性设为 1、aString 属性设为 String!、aList 属性设置为 [3.0]、然后调用 allDone()。

```dart
class BigObject {
  int anInt = 0;
  String aString = '';
  List<double> aList = [];
  bool _done = false;
  
  void allDone() {
    _done = true;
  }
}

BigObject fillBigObject(BigObject obj) {
  return obj?..anInt = 1
    ..aString = 'String!'
    ..aList = [3.0]
    ..allDone();
}
```

## 对象属性访问器：getters和setters
基础语法：按照类的封装原则，类属性不能直接暴露给外部访问或者设置，应该提供getters和setters方法。Dart提供了简单的实现方式。Dart的类属性没有public/private等可见于修饰符，如果以下划线_开头，则为private，否则为public公共域。

代码样例：有一个购物车类，其中有一个私有的 List<double> 类型的 prices 属性；一个名为 total 的 getter，用于返回总价格。只要新列表不包含任何负价格， setter 就会用新的列表替换列表（在这种情况下，setter 应该抛出 InvalidPriceException）。

```dart
class InvalidPriceException {}

class ShoppingCart {
  // 下划线开头，私有属性
  List<double> _prices = [];
  
  // total的getter方法，用户返回总价格值
  double get total => _prices.fold(0, (e,t) => e+t);

  // 设置_prices的值，如果存在负数，则抛出异常
  set prices(List<double> newPrices) {
    if(newPrices.any((e) => e < 0)) {
      throw InvalidPriceException();
    }
    
    _prices = newPrices;
  }
}
```

## 函数可选位置入参：[]
基础语法：函数的入参列表中，最后面的参数通过[]曝光起来，他们是可选的，即调用函数时可以不传入参数。除非指定了默认值，否则可选入参默的认值均为null。

```dart
void main() {
  // 8. 函数可选位置入参
  int sumUpToFive(int a, [int? b, int? c, int? d, int? e]) {
    return a + (b ?? 0) + (c ?? 0) + (d ?? 0) + (e ?? 0);
  }

  int sumUpToFive2(int a, [int b = 2, int c = 3, int d = 4, int e = 5]) {
    return a + b + c + d + e;
  }

  print('8. 函数可选位置入参: sumUpToFive(1,2)=${sumUpToFive(1, 2)}, sumUpToFive(1,2,3)=${sumUpToFive(1, 2, 3)}');
  print('8. 函数可选位置入参: sumUpToFive2(1,1)=${sumUpToFive2(1, 1)}, sumUpToFive2(1,1,1)=${sumUpToFive2(1, 1, 1)}');
  // 结果：
  // 8. 函数可选位置入参: sumUpToFive(1,2)=3, sumUpToFive(1,2,3)=6
  // 8. 函数可选位置入参: sumUpToFive2(1,1)=14, sumUpToFive2(1,1,1)=12
}
```

## 函数命名入参：{}
基本语法：与位置参数类似，命名参数使用{}包裹，它也是可选的，除非有默认值，否则它的值也是null。在调用命名参数函数时，命名参数必须通过参数名来定位，且它的顺序是可随意（这2点是与位置参数的最大区别）。

代码样例：有个MyDataObject类，有3个属性和copyWith方法，方法的入参均可能为空，如果为空则使用原对象值，否则使用入参值：

```dart
class MyDataObject {
  final int anInt;
  final String aString;
  final double aDouble;

  MyDataObject({
    this.anInt = 1,
    this.aString = 'OLD',
    this.aDouble = 2.0,
  });

  String toString() {
    return 'MyDataObject: {anInt=$anInt, aString=$aString, aDouble=$aDouble}';
  }

  // 本方法的3个入参均为命名参数
  MyDataObject copyWith({int? newInt, String? newString, double? newDouble}) {
    return MyDataObject(
      anInt: newInt ?? this.anInt,
      aString: newString ?? this.aString,
      aDouble: newDouble ?? this.aDouble,
    );
  }
}

void main() {
  // 10. 函数命名入参
  final myDataObject = MyDataObject();
  final newDataObject = myDataObject.copyWith(newInt: 2, newString: 'NEW', newDouble: 4.0);
  print('10. 函数命名入参: myDataObject=$myDataObject, newDataObject=$newDataObject');
  // 结果：10. 函数命名入参: myDataObject=MyDataObject: {anInt=1, aString=OLD, aDouble=2.0}, newDataObject=MyDataObject: {anInt=2, aString=NEW, aDouble=4.0}
}
```

## 异常：try,on,catch,rethrow和finally
基础语法：Dart可以抛出和捕获异常，所有异常都是未检测异常。函数或者方法无需声明可能抛出的异常。Dart提供了Exception和Error两种异常类型，但业务逻辑中，可以抛出任意非空的对象（如：throw 'abc'）。通过rethrow关键字，可重新抛出异常。

代码样例：tryFunction不可靠方法，捕获不同的异常并打印日志。

```dart
typedef VoidFunction = void Function();

class ExceptionWithMessage {
  final String message;
  const ExceptionWithMessage(this.message);
}

// Call logException to log an exception, and doneLogging when finished.
abstract class Logger {
  void logException(Type t, [String? msg]);
  void doneLogging();
}

void tryFunction(VoidFunction untrustworthy, Logger logger) {
  try {
    untrustworthy();
  } on ExceptionWithMessage catch(e) {
    logger.logException(e.runtimeType, e.message);
  } on Exception {
    logger.logException(Exception);
  } finally {
    logger.doneLogging();
  } 
}
```

## 构造方法：this,required,位置参数和命名参数
基础语法：在构造函数中，通过this关键字可以为成员变量快速赋值。构造函数的如此可以是位置参数，也可以是命名参数，如果参数是必选参数，则使用required关键字修饰，且该参数不能有默认值。

```dart
// 位置参数
class MyColor1 {
  int red;
  int green;
  int blue;

  // 主构造函数
  MyColor1(this.red, this.green, this.blue);

  // 命名构造函数：默认值初始化
  MyColor1.origin()
      : red = 0,
        green = 0,
        blue = 0;

  MyColor1.origin2() : this(0, 0, 0);
}

final color10 = MyColor1(80, 80, 128);
final color11 = MyColor1.origin();
final color12 = MyColor1.origin2();

// 命名参数
class MyColor2 {
  int red;
  int green;
  int blue;

  // 主构造函数
  MyColor2({required this.red, required this.green, required this.blue});

  // 命名构造函数：默认值初始化
  MyColor2.origin()
      : red = 0,
        green = 0,
        blue = 0;

  MyColor2.origin2() : this(red: 0, green: 0, blue: 0);
}

final color20 = MyColor2(
  red: 80,
  green: 80,
  blue: 128,
);
final color21 = MyColor2.origin();
final color22 = MyColor2.origin2();
```

## 构造方法：:初始化列表
基础语法：在执行构造函数体之前，需要进行一些初始化操作，比如校验参数合法性、初始化参数等。

代码样例：使用的初始化列表将 word 的前两个字符分配给 letterOne 和 LetterTwo 属性。

```dart
class FirstTwoLetters {
  final String letterOne;
  final String letterTwo;

  // 初始化列表
  FirstTwoLetters(String word)
      : assert(word.length >= 2),
        letterOne = word[0],
        letterTwo = word[1];

  String toString() {
    return 'FirstTwoLetters: {letterOne=$letterOne, letterTwo=$letterTwo}';
  }
}

void main() {
  // 13. 构造方法：初始化列表
  final firstTwoLetters = FirstTwoLetters('Dart');
  print('13. 构造方法：初始化列表: firstTwoLetters=$firstTwoLetters');
  // 结果：13. 构造方法：初始化列表: firstTwoLetters=FirstTwoLetters: {letterOne=D, letterTwo=a}
}
```

## 构造方法：factory工厂
基础语法：父类根据入参，返回具体子类。

代码样例：一般父类方法提供一个无任何参数的构造函数。

```dart
class Square extends Shape {}

class Circle extends Shape {}

class Shape {
  Shape();

  factory Shape.fromTypeName(String typeName) {
    if (typeName == 'square') return Square();
    if (typeName == 'circle') return Circle();

    throw ArgumentError('Unrecognized $typeName');
  }
}
```

## 构造方法：:重定向
基本语法：构造方法中，通过:引用另外一个构造方法，可以是主构造函数，也可以是命名构造函数。

```dart
class Automobile {
  String make;
  String model;
  int mpg;

  // 类主构造函数
  Automobile(this.make, this.model, this.mpg);

  // 命名构造函数：重定向主构造函数
  Automobile.hybrid(String make, String model) : this(make, model, 60);

  // 命名构造函数：重定向命名构造函数
  Automobile.fancyHybrid() : this.hybrid('Futurecar', 'Mark 2');
}
```

## 构造方法：final,const常量
基础语法：如果类生成的对象永远都不会更改，则可以让这些对象成为编译时常量。为此，请定义 const 构造方法并确保所有实例变量都是 final 的。

```dart
class ImmutablePoint {
  static const ImmutablePoint origin = ImmutablePoint(0, 0);

  // 类属性必须用final修饰
  final int x;
  final int y;

  // 构造函数更加const关键字
  const ImmutablePoint(this.x, this.y);
}
```

## 最后
Dart学习第2天，根据官方文档由浅入深学习，更多语法和技巧在后续研发中我在补充。

完整的测试用的实例代码，部分代码示例在小节中已经提供：

```dart
class MyDataObject {
  final int anInt;
  final String aString;
  final double aDouble;

  MyDataObject({
    this.anInt = 1,
    this.aString = 'OLD',
    this.aDouble = 2.0,
  });

  String toString() {
    return 'MyDataObject: {anInt=$anInt, aString=$aString, aDouble=$aDouble}';
  }

  // 本方法的3个入参均为命名参数
  MyDataObject copyWith({int? newInt, String? newString, double? newDouble}) {
    return MyDataObject(
      anInt: newInt ?? this.anInt,
      aString: newString ?? this.aString,
      aDouble: newDouble ?? this.aDouble,
    );
  }
}

// 位置参数
class MyColor1 {
  int red;
  int green;
  int blue;

  // 主构造函数
  MyColor1(this.red, this.green, this.blue);

  // 命名构造函数：默认值初始化
  MyColor1.origin()
      : red = 0,
        green = 0,
        blue = 0;

  MyColor1.origin2() : this(0, 0, 0);
}

final color10 = MyColor1(
  80,
  80,
  128,
);
final color11 = MyColor1.origin();
final color12 = MyColor1.origin2();

// 命名参数
class MyColor2 {
  int red;
  int green;
  int blue;

  // 主构造函数
  MyColor2({required this.red, required this.green, required this.blue});

  // 命名构造函数：默认值初始化
  MyColor2.origin()
      : red = 0,
        green = 0,
        blue = 0;

  MyColor2.origin2() : this(red: 0, green: 0, blue: 0);
}

final color20 = MyColor2(
  red: 80,
  green: 80,
  blue: 128,
);
final color21 = MyColor2.origin();
final color22 = MyColor2.origin2();

class FirstTwoLetters {
  final String letterOne;
  final String letterTwo;

  // 初始化列表
  FirstTwoLetters(String word)
      : assert(word.length >= 2),
        letterOne = word[0],
        letterTwo = word[1];

  String toString() {
    return 'FirstTwoLetters: {letterOne=$letterOne, letterTwo=$letterTwo}';
  }
}

class ImmutablePoint {
  static const ImmutablePoint origin = ImmutablePoint(0, 0);

  // 类属性必须用final修饰
  final int x;
  final int y;

  // 构造函数更加const关键字
  const ImmutablePoint(this.x, this.y);
}

void main() {
  // 1. 字符串插值
  var a = 2;
  var b = 3;
  var c = 'Hello';
  print(
      '1. 字符串插值: ${c.toUpperCase()} Dart:a is ${a} and b is ${b}, so a>b is ${a > b}, a+b=${a + b}');

  // 2. 变量赋值
  // 非法声明：int d = null; String s;
  int d = 4;
  int? e;
  String f = 'Hello';
  String? g;
  print('2. 变量赋值: d: ${d}, e:${e}, f:${f}, g:${g}');

  // 3. 空运算符
  int? h;
  h ??= 3;
  h ??= 5; // 不生效，因为此时h非空
  int i = 1 ?? 3; // 1非空，所以3不生效
  int j = null ?? 4;
  print('3. 空运算符: h=${h}, i=${i}, j=${j}');

  // 4. 访问空对象属性
  String? k;
  String? l = 'Hello';
  var m = k?.toLowerCase()?.toUpperCase();
  var n = l?.toLowerCase()?.toUpperCase();
  print('4. 访问空对象属性: m=${m}, n=${n}');

  // 5. 集合类型
  var aList = <String>['a', 'b', 'b'];
  var aSet = <String>{'a', 'b', 'b'};
  var aMap = <String, String>{'a': 'Hello', 'b': 'World', 'b': 'Dart'};
  print('5. 集合类型: aList=${aList}, aSet=${aSet}, aMap=${aMap}');

  // 6. 箭头语法函数
  String joinWithCommas(List<int> values) => values.join(',');
  final cList = <int>[2, 5, 7];
  print('6. 箭头语法函数: cList join=${joinWithCommas(cList)}');

  // 8. 函数可选位置入参
  int sumUpToFive(int a, [int? b, int? c, int? d, int? e]) {
    return a + (b ?? 0) + (c ?? 0) + (d ?? 0) + (e ?? 0);
  }

  int sumUpToFive2(int a, [int b = 2, int c = 3, int d = 4, int e = 5]) {
    return a + b + c + d + e;
  }

  print(
      '8. 函数可选位置入参: sumUpToFive(1,2)=${sumUpToFive(1, 2)}, sumUpToFive(1,2,3)=${sumUpToFive(1, 2, 3)}');
  print(
      '8. 函数可选位置入参: sumUpToFive2(1,1)=${sumUpToFive2(1, 1)}, sumUpToFive2(1,1,1)=${sumUpToFive2(1, 1, 1)}');

  // 10. 函数命名入参
  final myDataObject = MyDataObject();
  final newDataObject = myDataObject.copyWith(newInt: 2, newString: 'NEW', newDouble: 4.0);
  print('10. 函数命名入参: myDataObject=$myDataObject, newDataObject=$newDataObject');

  // 13. 构造方法：初始化列表
  final firstTwoLetters = FirstTwoLetters('Dart');
  print('13. 构造方法：初始化列表: firstTwoLetters=$firstTwoLetters');
}
```
