+++
slug = "2023102801"
date = "2023-10-28"
lastmod = "2023-10-28"
title = "Flutter/Dart第18天：Dart特性之可调用对象"
description = "今天我们来看看Dart语言的一个有趣的特性——可调用对象。对象也可以像函数那样被调用，这个特性是怎么用的呢……"
image = "00.jpg"
tags = [ "Dart", "Flutter", "跨平台", "类" ]
categories = [ "专业技术" ]
+++

Dart官方文档：[https://dart.dev/language/callable-objects](https://dart.dev/language/callable-objects)

<b>重要说明：</b>本博客基于Dart官网文档，但并不是简单的对官网进行翻译，在覆盖核心功能情况下，我会根据个人研发经验，加入自己的一些扩展问题和场景验证。

<b>可调用对象：</b>Dart实例如果实现了`call()`方法，那么实例就可以像函数那样被调用，这个对象就被称为可调用对象。`call()`方法可以定义在任何类中，让类实例像函数一样被调用。这个函数和普通函数没有区别，包括参数和返回值等。

如下代码样例，`WannabeFunction`类定义了`call()`方法，方法入参是3个字符串，方法返回结果是一个字符串：

```dart
class WannabeFunction {
  String call(String a, String b, String c) => '$a $b $c!';
}

var wf = WannabeFunction();
var out = wf('Hi', 'NTopic,', 'CN');

void main() => print(out);
// 结果：Hi NTopic, CN!
```

Dart语言的可调用对象的应用场景，我目前还没有涉及到，恳请各位网友帮忙补充，非常感谢 👍🏻

---
我的本博客原地址：[https://ntopic.cn/p/2023102801](https://ntopic.cn/p/2023102801/)

---
