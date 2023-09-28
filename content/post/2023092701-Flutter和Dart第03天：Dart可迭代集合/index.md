+++
slug = "2023092701"
date = "2023-09-27"
lastmod = "2023-09-27"
title = "Flutter/Dart第03天：Dart可迭代集合"
description = "在Dart学习的第02天，我们通过基础语法说明和样例代码的方式，学习了Dart的16个基础语法，这些基础语法给我们后面编写的Flutter程序打下来坚实基础。今天，我们继续深入学习Dart乃至所有编程语言都非常重要的部分：可迭代的集合……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台" ]
categories = [ "专业技术" ]
+++

Dart官网代码实验室：[https://dart.dev/codelabs/iterables](https://dart.dev/codelabs/iterables)

<b>重要说明：</b>本博客基于Dart官网代码实验室，但并不是简单的对官网文章进行翻译，我会根据个人研发经验，在覆盖官网文章核心内容情况下，加入自己的一些扩展问题和问题演示和总结，包括名称解释、使用场景说明、代码样例覆盖等。

## 可迭代集合说明
什么是集合？集合代表一组对象的组合，集合中的对象一般称为元素，元素的数量可以是0个（即空集合），也可以有多个。

什么是迭代？迭代即顺序访问，即这个集合中的元素可从头到尾进行顺序访问（一般在循环遍历中使用）。在Java中，我们知道有个Iterable迭代类，在Dart中也有这个类（[https://api.dart.dev/stable/3.1.3/dart-core/Iterable-class.html](https://api.dart.dev/stable/3.1.3/dart-core/Iterable-class.html)），我们用的最多的就是List和Set接口，他们是迭代集合的基础，也是一个应用程序的基础。

Map是可迭代集合吗？Map类代表了一组元素，因此它是一个集合。但Map类没有实现Iterable类（[https://api.dart.dev/stable/3.1.3/dart-core/Map-class.html](https://api.dart.dev/stable/3.1.3/dart-core/Map-class.html)），因此它不可迭代，也就是说：Map是不可迭代的集合。但是它的元素集合（Map#entries）、键集合（Map#keys）和值集合（Map#values）都是可迭代集合。

迭代和集合访问元素的不同方式：迭代通过`elementAt(index)`方法访问元素，而集合可以通过`[index]`下标的方法访问元素：

```dart
void main() {
  // 1. 迭代和集合访问元素
  final List<int> alist = [1, 2, 3];
  final Iterable<int> iterable = alist;
  print('1. 迭代和集合访问元素: ${iterable.elementAt(2)} <-> ${alist[2]}');
  
  // 结果：1. 迭代和集合访问元素: 3 <-> 3
}
```

## 可迭代集合元素访问方法
可迭代集合元素的访问方式有很多种，包括for循环，集合的第1个元素，集合的最后1个元素，寻找符合条件的第1个元素等。

### for-in循环访问集合元素
这种方式使用最多了，各种编程语言基本类似：

```dart
void main() {
  final List<int> alist = [1, 2, 3];
  
  // 2.1. for循环访问集合元素
  for (final element in alist) {
    print('2.1. for循环访问集合元素: $element');
  }
  
  // 结果：
  // 2.1. for循环访问集合元素: 1
  // 2.1. for循环访问集合元素: 2
  // 2.1. for循环访问集合元素: 3
}
```

### first第一个和last最后一个元素：空集合异常
通过first和last属性，可直接访问集合的第1个和最后1个元素：

```dart
void main() {
  final List<int> alist = [1, 2, 3];

  // 2.2 first第一个和last最后一个元素
  print('2.2. first第一个和last最后一个元素: first=${alist.first}, last=${alist.last}');
  
  // 结果：2.2. first第一个和last最后一个元素: first=1, last=3
}
```

<b>扩展问题：</b>如果集合只有1个元素，或者集合是空集合，first和last返回的内容是什么呢？

```dart
void main() {
  final List<int> oneList = [1];
  print('2.2. first第一个和last最后一个元素: one.first=${oneList.first}, one.last=${oneList.last}');
  
  // 结果：2.2. first第一个和last最后一个元素: one.first=1, one.last=1
  
  final List<int> emptyList = [];
  print('2.2. first第一个和last最后一个元素: empty.first=${emptyList.first}, empty.last=${emptyList.last}');
  
  // 结果：Bad state: No element
}

// 异常如下：
Unhandled exception:
Bad state: No element
#0      List.first (dart:core-patch/growable_array.dart:343:5)
```

<b>结论：</b>只有1个元素的集合，first和last返回值相同，均为唯一的那个元素；对于空集合，first或者last均抛出异常！

### firstWhere()/orElse符合条件的第1个元素
<b>断言：</b>一个返回true/false的表达式、方法或者代码块。firstWhere()的本质就是遍历集合，对每个元素进行断言，然后返回第一个断言为true的元素。

```dart
void main() {
  final List<int> alist = [1, 2, 3];

  // 2.3. firstWhere()符合条件的第1个元素
  final int firstWhere = alist.firstWhere((element) => element > 1);
  print('2.3. firstWhere()符合条件的第1个元素: $firstWhere');

  // 结果：2.3. firstWhere()符合条件的第1个元素: 2
}
```

<b>扩展问题：</b>如果过滤条件均不满足（即每个元素断言均返回false），则返回结果是什么呢？

```dart
void main() {
  final List<int> alist = [1, 2, 3];

  // 2.3. firstWhere()符合条件的第1个元素
  final int firstWhere2 = alist.firstWhere((element) => element > 3);
  print('2.3. firstWhere()符合条件的第1个元素2: $firstWhere2');

  // 结果：Bad state: No element
}

// 异常如下：
Unhandled exception:
Bad state: No element
#0      ListBase.firstWhere (dart:collection/list.dart:132:5)
```

<b>结论：</b>和空集合一样，当firstWhere()无法匹配到任何元素时，会抛出异常！

那么，当无法匹配到任何元素时，有没有办法不抛出异常，而是返回一个默认值呢？

答案是有的：firstWhere()断言之后，增加<b>orElse</b>默认值的命名参数，它是一个函数！

```dart
void main() {
  final List<int> alist = [1, 2, 3];

  // 2.3. firstWhere()符合条件的第1个元素
  final int firstWhere2 = alist.firstWhere(
    (element) => element > 3,
    orElse: () => -1,
  );
  print('2.3. firstWhere()符合条件的第1个元素2: $firstWhere2');

  // 结果：2.3. firstWhere()符合条件的第1个元素2: -1
}
```

### any()/every()集合检测（有趣的结果和源代码）
当我们需要检测集合中是否存在符合某个条件的元素，或者所有元素是否符合某个条件。在Dart语言中，我们可以使用any()和every()这两个集合条件检测方法，来达到我们的目的。
 - any()方法：集合中存在任一一个元素符合条件
 - every()方法：集合中的所有元素均符合条件

```dart
void main() {
  final List<int> alist = [1, 2, 3];

  // 2.4. any()/every()集合条件检测
  final bool anyGtTwo = alist.any((element) => element > 2);
  final bool everyGtZero = alist.every((element) => element > 0);
  final bool everyGtTwo = alist.every((element) => element > 2);
  print('2.4. any()/every()集合条件检测: anyGtTwo=$anyGtTwo, everyGtZero=$everyGtZero, everyGtTwo=$everyGtTwo');

  // 结果：2.4. any()/every()集合条件检测: anyGtTwo=true, everyGtZero=true, everyGtTwo=false
}
```

<b>扩展问题：</b>如果是个空集合，any()和every()的结果如何，会抛出异常吗？

```dart
void main() {
  final elist = <int>[];
  final bool anyGtZero = elist.any((element) => element > 0);
  final bool anyLtEqZero = elist.any((element) => element <= 0);
  print('2.4. any()-空集合条件检测: anyGtZero=$anyGtZero, anyLtEqZero=$anyLtEqZero');

  // 结果：2.4. any()-空集合条件检测: anyGtZero=false, anyLtEqZero=false

  final bool evyGtZero = elist.every((element) => element > 0);
  final bool evyLtEqZero = elist.every((element) => element <= 0);
  print('2.4. every()-空集合条件检测: evyGtZero=$evyGtZero, evyLtEqZero=$evyLtEqZero');

  // 结果：2.4. every()-空集合条件检测: evyGtZero=true, evyLtEqZero=true
}
```

<b>有趣的结论：</b>
1. 针对空集合，any()和every()这2个集合条件检测方法，不会抛出异常！
2. 针对空集合，any()不论断言结果如何，返回值均为false（这个结果比较容易理解）
3. 针对空集合，every()不论断言结果如何，返回值均为true（这个结果有的奇怪！）

我们打开源代码，看看any()和every()的发现：any()的默认返回值为false，every()的默认返回值true

```dart
abstract mixin class Iterable<E> {
  // 默认返回值：false
  bool any(bool test(E element)) {
    for (E element in this) {
      if (test(element)) return true;
    }
    return false;
  }

  // 默认返回值：true
  bool every(bool test(E element)) {
    for (E element in this) {
      if (!test(element)) return false;
    }
    return true;
  }
}
```

### where()/takeWhile()/skipWhile()筛选子集合
上面的any()/every()只是一个条件判断，本节来看看，通过条件来筛选子集合：

 - <b>where()方法：</b>遍历集合每个元素，返回所有符合条件的子集合；若所有元素均不符合条件，则返回空集合，而不是抛出异常！
 - <b>takeWhile()方法：</b>遍历集合元素，构成新子集合元素，直到不符合条件的元素为止。
 - <b>skipWhile()方法：</b>与takeWhile()相反，遍历集合元素，过滤掉前面所有符合条件元素，取结合后面元素。

```dart
void main() {
  final List<int> alist = [1, 2, 3];
  
  // 2.5. where()/takeWhile()/skipWhile()筛选子集合
  final gtOneList = alist.where((element) => element > 1);
  print('2.5. where()-筛选子集合: gtOneList=$gtOneList');

  // 结果：2.5. where()-筛选子集合: gtOneList=(2, 3)

  final takeLtThreeList = alist.takeWhile((element) => element < 3);
  print('2.5. takeWhile()-筛选子集合: takeLtTwoList=$takeLtThreeList');

  // 结果：2.5. takeWhile()-筛选子集合: takeLtTwoList=(1, 2)

  final skipNeqTwoList = alist.skipWhile((element) => element != 2);
  print('2.5. skipWhile()-筛选子集合: skipNeqTwoList=$skipNeqTwoList');

  // 结果：2.5. skipWhile()-筛选子集合: skipNeqTwoList=(2, 3)
}
```

### map()转换集合元素
对集合的每个元素，通过map()函数进行一次计算，可把一个结合转换为另一个元素的集合。

```dart
void main() {
  final List<int> alist = [1, 2, 3];

  // 2.6. map()转换集合元素
  final alistPlus3 = alist.map((e) => 3 + e);
  print('2.6. map()转换集合元素: $alist -> $alistPlus3');

  // 结果：2.6. map()转换集合元素: [1, 2, 3] -> (4, 5, 6)

  final intToStringList = alist.map((e) => 'value:$e');
  print('2.6. map()转换集合元素: $alist -> $intToStringList');

  // 结果：2.6. map()转换集合元素: [1, 2, 3] -> (value:1, value:2, value:3)
}
```

## 最后总结
<b>特别注意：</b>
1. first/last/firstWhere()这些返回值为单个元素方法，当为空集合或者无法找到元素时，会抛出异常。
2. where()/takeWhile()/skipWhile()这些返回值为集合的方法，当为空集合或者无法匹配到元素，返回空集合，不会抛出异常。
3. any()默认返回值为false，every()默认返回值为true

---
我的本博客原地址：[https://ntopic.cn/p/2023092701/](https://ntopic.cn/p/2023092701)

---
