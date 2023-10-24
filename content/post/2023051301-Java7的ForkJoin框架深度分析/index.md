+++
slug = "2023051301"
date = "2023-05-13"
lastmod = "2023-05-13"
title = "Java ForkJoin框架分析和实战"
description = "Java 7的JUC包（java.util.concurrent）实现了高并发编程的Fork/Join框架，且该框架还是由「Doug Lea」大神亲自操刀编写，今天你还会使用吗？"
#image = "https://picsum.photos/id/201/1600/400.jpg"
image = "images/01.jpg"
tags = [ "Java", "并发编程", "ForkJoin框架" ]
categories = [ "专业技术" ]
+++

## Fork/Join框架：总分总思路
Java的Doug Lea大神在Java 7的JUC包中，已经实现了Fork/Join框架。

该框架特别适合`总-分-总`的使用场景，类似于MapReduce思想：将大任务拆分成若干个小任务，最终汇总每个小任务的结果后得到最终大任务的结果。每个小任务直接相互独立。

## Fork/Join框架：2个核心类
Fork/Join框架的核心只有两个：**ForkJoinPool**和**ForkJoinTask**

* **ForkJoinPool**主要负责实现工作窃取算法、管理工作线程、提供关于任务的状态以及执行信息。
* **ForkJoinTask**主要提供在任务中执行Fork（拆分任务）和Join（汇总任务）操作的机制。

## Fork/Join框架实战：数值累加
**任务目标：** 累加给定的a~b数字区间

**Fork/Join实现思路：**
* 设定一个阈值，每个任务的计算量超过这个阈值，则进行任务拆分
* 当拆分了子任务时，当前任务的结果需要汇总子任务的结果
* 一直递归下去

***

**ForkJoinTask任务实现：**
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.example;

import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinTask--数据计算样例：各一个数字区间，计算数字的累加值！
 *
 * 基本思路--总体为`总-分-总`思想，类似于MapReduce思路：
 *   1. 拆分任务：根据当前任务参数，决策是否需要进行任务拆分；如果需要拆分，则本任务的结果为所有拆分任务的汇总
 *   2. 汇总任务：根据第1点思路，第1个任务为总任务-拆分子任务-汇总子任务结果，那么第1个任务的值就是最终的值
 *
 * @author obullxl 2023年05月13日: 新增
 */
public class CalculateForkJoinTask extends RecursiveTask<Integer> {
    /** 任务拆分的阈值，超过该值则任务需要拆分*/
    public static final int THRESHOLD = 10;

    /**
     * 任务参数：数据计算的开始值
     */
    private final int start;

    /**
     * 任务参数：数据计算的结束值
     */
    private final int finish;

    public CalculateForkJoinTask(int start, int finish) {
        this.start = start;
        this.finish = finish;
    }

    @Override
    public Integer compute() {
        int sum = 0;

        // 检测单个任务计算量是否符合阈值，如果超过了的话进行任务拆分
        if ((this.finish - this.start) <= THRESHOLD) {
            for (int i = start; i <= finish; i++) {
                sum += i;
            }
        } else {
            // 单个任务量超过阈值，则进行任务拆分：这里是拆成了2个任务，可根据业务实际情况拆出多个任务
            int middle = (this.start + this.finish) / 2;

            RecursiveTask<Integer> leftTask = new CalculateForkJoinTask(this.start, middle);
            RecursiveTask<Integer> rightTask = new CalculateForkJoinTask(middle + 1, this.finish);

            // 执行每一个子任务：这里只有2个子任务
            leftTask.fork();
            rightTask.fork();

            // 等待并获取每个子任务执行的结束
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();

            // 合并子任务的执行结果
            sum = leftResult + rightResult;
        }

        // 本任务的结果：可能是最终的子任务，也可能是多个子任务是汇总结果
        return sum;
    }
}
```
***
**ForkJoinTask的实战验证：**
> 验证点：<br/>
> 1~100累加值=5050
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.example;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * CalculateForkJoinTask--测试验证
 *
 * @author obullxl 2023年05月13日: 新增
 */
public class CalculateForkJoinTaskTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        // 构建任务：累加1~100值
        RecursiveTask<Integer> task = new CalculateForkJoinTask(1, 100);

        // 执行任务
        Future<Integer> result = ForkJoinPool.commonPool().submit(task);

        // 验证结果
        Assert.assertEquals(5050, result.get().intValue());
    }
}
```

---
我的本博客原地址：[https://ntopic.cn/p/2023051301](https://ntopic.cn/p/2023051301/)

---
