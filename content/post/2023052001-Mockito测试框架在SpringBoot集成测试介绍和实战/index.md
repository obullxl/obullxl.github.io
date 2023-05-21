+++
slug = "2023052001"
date = "2023-05-20"
lastmod = "2023-05-21"
title = "Mockito测试框架在SpringBoot集成测试中的介绍和实战"
description = "当系统依赖了另外系统时，为了保障本系统的集成测试用例集的运行稳定性和提升用例场景的覆盖面，最常见的做法是模拟依赖系统服务，以保障本系统功能逻辑的自闭环，让集成测试更聚焦于本系统的功能逻辑验证……"
image = "images/01.jpg"
tags = [ "Java", "Mockito", "研发质量", "集成测试" ]
categories = [ "专业技术", "研发质量" ]
+++

## 集成测试为代码质量提供保障
&emsp;&emsp;在进行系统迭代升级改造研发过程中，如何保障本次迭代改动的代码逻辑符合预期？如何保障本次迭代改动逻辑对原逻辑的影响面？一种办法是人工把端到端用例全部回归一遍（如通过页面入口、服务接口工具等），这种办法的效率低下且容易遗漏（如人工遗漏用例，页面入口不可见等）；另外一种办法就是通过集成测试用例集，通过精心设计的且运行稳定的集成测试用例集，为迭代研发的质量提供了强有力的保障，特别适用于纯接口类或者平台类系统！

&emsp;&emsp;当一个系统的服务依赖另外一个系统的服务时，集成测试用例集的稳定运行和本系统的鲁棒性存在一定挑战：如在测试环境中依赖系统的服务不稳定可能导致集成测试用例的结果不稳定，如依赖系统服务的各个场景无法全面覆盖（如特定的错误码、数据查询的结果无法预知等）


## Mockito框架可保障依赖服务稳定性
&emsp;&emsp;为了保障集成测试用例集的运行稳定性和提升用例场景的覆盖面，最常见的做法是模拟依赖系统服务（即集成测试执行并未真正调用依赖系统的服务，而是为达到测试目的对于服务进行模拟），以保障本系统功能逻辑的自闭环（即不依赖其他系统服务），让集成测试更聚焦于本系统的功能逻辑验证。

&emsp;&emsp;在Spring/SpringBoot框架中，Mockito框架为集成测试用例执行模拟依赖服务提供了多种能力，基本覆盖了我们日常集成测试需求：
> 1. 正常场景：模拟正常返回结果
> 2. 异常场景：模拟执行异常
> 3. 内部逻辑验证：
>   a. 检测被模拟方法的执行次数
>   b. 检测被模拟方法的执行入参内容

## Mockito框架测试使用实战
**实战场景：** 订单查询服务可根据请求订单ID列表参数，对订单ID进行过滤，并调用另外远程客户端查询数据并返回订单信息，本服务存在以下逻辑点
> 1. 订单ID过滤逻辑：属于本系统逻辑，需要测试覆盖
> 2. 远程订单查询服务：依赖服务，服务不可控，应该进行模拟

***

**SpringBoot测试框架** 的注解：
```java
@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class NTAbstractTest extends AbstractJUnit4SpringContextTests {
```

**Mockito依赖类** 的注解：Spring测试框架启动过程中，对于被 **@MockBean**注解的属性，在Spring上下文中替换原有真实的Bean，即被替换成 **Mockito.mock(xx.class)** 
```java
    /**
     * Mock对象
     */
    @MockBean
    @Autowired
    @Qualifier("orderQueryClient")
    private OrderQueryClient orderQueryClient;
```

***

测试目标类 - 订单查询服务接口（**OrderQueryService.java**）：
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order;

import cn.ntopic.ListResult;
import cn.ntopic.order.model.OrderModel;

import java.util.List;

/**
 * 订单查询服务
 *
 * @author obullxl 2023年05月20日: 新增
 */
public interface OrderQueryService {

    /**
     * 根据订单ID查询订单列表，仅返回最近3个月的ID信息
     *
     * @param orderIdList 订单ID列表
     * @return 最近3个月的订单信息
     */
    ListResult<OrderModel> findList(List<String> orderIdList);

}
```

测试目标类 - 订单查询服务逻辑（**OrderQueryServiceImpl.java**）：
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order;

import cn.ntopic.ListResult;
import cn.ntopic.order.dto.OrderDTO;
import cn.ntopic.order.model.OrderModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单查询服务实现
 *
 * @author obullxl 2023年05月20日: 新增
 */
@Component("orderQueryService")
public class OrderQueryServiceImpl implements OrderQueryService {

    /**
     * 订单查询客户端，远程服务不可控
     */
    private final OrderQueryClient orderQueryClient;

    public OrderQueryServiceImpl(@Qualifier("orderQueryClient") OrderQueryClient orderQueryClient) {
        this.orderQueryClient = orderQueryClient;
    }

    @Override
    public ListResult<OrderModel> findList(List<String> orderIdList) {
        // 参数校验
        if (CollectionUtils.isEmpty(orderIdList)) {
            return new ListResult<>(true);
        }

        // 订单ID过滤：假设仅支持3个月内容的订单
        List<String> destOrderIdList = orderIdList.stream().filter(this::filterOrderId).collect(Collectors.toList());

        ListResult<OrderModel> orderResult;
        try {
            // 查询订单信息
            List<OrderDTO> orderList = this.orderQueryClient.queryList(destOrderIdList);

            // 订单模型转换
            List<OrderModel> modelList = orderList.stream().map(OrderModel::from).collect(Collectors.toList());

            // 返回成功结果
            orderResult = new ListResult<>(true);
            orderResult.setResultObj(modelList);
        } catch (Throwable e) {
            // 测试日志输出
            orderResult = new ListResult<>(false);
            orderResult.setCode("UNKNOWN_ERROR");
            orderResult.setMessage(e.getMessage());
        }

        return orderResult;
    }

    /**
     * yyyyMMdd日期检查：订单ID格式为日期开头，且日期为最近3个月内
     */
    private boolean filterOrderId(String orderId) {
        if (StringUtils.length(orderId) < 8) {
            return false;
        }

        // 3个月的日期
        LocalDate minOrderDate = LocalDate.now().minusMonths(3L);

        // 当前订单的日期
        LocalDate orderDate;
        try {
            String prefix = StringUtils.substring(orderId, 0, 8);
            orderDate = LocalDate.parse(prefix, DateTimeFormatter.BASIC_ISO_DATE);

            // 比较是否为3个月之内
            return !orderDate.isBefore(minOrderDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
```

被模拟对象 - 远程订单查询服务接口（**OrderQueryClient.java**）：
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order;


import cn.ntopic.order.dto.OrderDTO;

import java.util.List;

/**
 * 订单查询客户端
 *
 * @author obullxl 2023年05月20日: 新增
 */
public interface OrderQueryClient {

    /**
     * 查询订单列表
     *
     * @param orderIdList 订单ID列表
     * @return 订单基本信息
     */
    List<OrderDTO> queryList(List<String> orderIdList);
}
```

> 其他的依赖类见本文末尾附录！

集成测试用例（**OrderQueryServiceTest.java**）：
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.example.mockito;

import cn.ntopic.ListResult;
import cn.ntopic.NTAbstractTest;
import cn.ntopic.order.OrderQueryClient;
import cn.ntopic.order.OrderQueryService;
import cn.ntopic.order.dto.OrderDTO;
import cn.ntopic.order.enums.OrderStatusEnum;
import cn.ntopic.order.model.OrderModel;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * OrderService-单元测试
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class OrderQueryServiceTest extends NTAbstractTest {

    /**
     * 测试对象
     */
    @Autowired
    @Qualifier("orderQueryService")
    private OrderQueryService orderQueryService;

    /**
     * Mock对象
     */
    @MockBean
    @Autowired
    @Qualifier("orderQueryClient")
    private OrderQueryClient orderQueryClient;

    /**
     * Case01-正常返回：测试过滤3个月内订单逻辑
     */
    @Test
    public void test_queryList_01() {
        final String orderId1 = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "0001";
        final String orderId2 = LocalDate.now().minusMonths(3L).format(DateTimeFormatter.BASIC_ISO_DATE) + "0002";
        final String orderId3 = LocalDate.now().minusMonths(3L).minusDays(1L).format(DateTimeFormatter.BASIC_ISO_DATE) + "0003";

        // 1. Mock客户端返回结果
        // 正常不过滤的订单
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId(orderId1);
        orderDTO1.setAmount("3.33");
        orderDTO1.setStatus(OrderStatusEnum.WAIT_PAY.getCode());

        // 刚好3个月的临界值，不过滤的订单
        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setId(orderId2);
        orderDTO2.setAmount("4.44");
        orderDTO2.setStatus(OrderStatusEnum.FINISHED.getCode());

        Mockito.when(this.orderQueryClient.queryList(Mockito.anyList())).thenReturn(Lists.newArrayList(orderDTO1, orderDTO2));

        // 2. 执行查询逻辑
        ListResult<OrderModel> orderResult = this.orderQueryService.findList(Lists.newArrayList(orderId1, orderId2, orderId3));

        // 3. 检查执行结果：正常返回，orderId3被过滤
        Assert.assertTrue(orderResult.isSuccess());

        List<OrderModel> modeList = orderResult.getResultObj();
        Assert.assertEquals(2, modeList.size());

        ArgumentCaptor<List<String>> captorOrderIdList = ArgumentCaptor.forClass(List.class);
        Mockito.verify(this.orderQueryClient, Mockito.times(1)).queryList(captorOrderIdList.capture());
        List<String> queryOrderIdList = captorOrderIdList.getValue();
        Assert.assertEquals(2, queryOrderIdList.size());
        Assert.assertTrue(queryOrderIdList.contains(orderId1));
        Assert.assertTrue(queryOrderIdList.contains(orderId2));
    }

    /**
     * Case02-客户端异常：测试订单客户端异常逻辑
     */
    @Test
    public void test_queryList_02() {
        final String orderId1 = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "0001";
        final String orderId2 = LocalDate.now().minusMonths(3L).format(DateTimeFormatter.BASIC_ISO_DATE) + "0002";
        final String orderId3 = LocalDate.now().minusMonths(3L).minusDays(1L).format(DateTimeFormatter.BASIC_ISO_DATE) + "0003";
        final String errorMessage = "Mock异常";

        // 1. Mock客户端抛出异常
        Mockito.when(this.orderQueryClient.queryList(Mockito.anyList())).thenThrow(new RuntimeException(errorMessage));

        // 2. 执行查询逻辑
        ListResult<OrderModel> orderResult = this.orderQueryService.findList(Lists.newArrayList(orderId1, orderId2, orderId3));

        // 3. 检查执行结果：查询异常，orderId3被过滤
        Assert.assertFalse(orderResult.isSuccess());
        Assert.assertEquals("UNKNOWN_ERROR", orderResult.getCode());
        Assert.assertEquals(errorMessage, orderResult.getMessage());

        ArgumentCaptor<List<String>> captorOrderIdList = ArgumentCaptor.forClass(List.class);
        Mockito.verify(this.orderQueryClient, Mockito.atLeast(1)).queryList(captorOrderIdList.capture());
        List<String> queryOrderIdList = captorOrderIdList.getValue();
        Assert.assertEquals(2, queryOrderIdList.size());
        Assert.assertTrue(queryOrderIdList.contains(orderId1));
        Assert.assertTrue(queryOrderIdList.contains(orderId2));
    }
}
```

## 附录--其他代码
**NTAbstractTest.java**
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2022 All Rights Reserved.
 */
package cn.ntopic;

import cn.ntopic.core.xml.XMLNode;
import cn.ntopic.core.xml.XMLUtils;
import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

/**
 * @author obullxl 2022年01月09日: 新增
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class NTAbstractTest extends AbstractJUnit4SpringContextTests {
}
```

**Result.java**
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic;

import cn.ntopic.core.builder.ToString;

/**
 * 请求结果包装器
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class Result<T> extends ToString {

    /**
     * 结果成功标识
     */
    private final boolean success;

    /**
     * 结果返回码（如失败错误码、幂等成功标识等）
     */
    private String code;

    /**
     * 结果返回描述
     */
    private String message;

    /**
     * 结果对象
     */
    private T resultObj;

    public Result(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResultObj() {
        return resultObj;
    }

    public void setResultObj(T resultObj) {
        this.resultObj = resultObj;
    }
}
```

**ListResult.java**
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic;

import java.util.List;

/**
 * 请求结果列表包装器
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class ListResult<T> extends Result<List<T>> {

    public ListResult(boolean success) {
        super(success);
    }

}
```

**OrderDTO.java**
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order.dto;

import cn.ntopic.core.builder.ToString;

/**
 * 订单基本信息
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class OrderDTO extends ToString {
    /**
     * 订单ID，非空
     */
    private String id;

    /**
     * 订单金额
     */
    private String amount;

    /**
     * 订单状态
     */
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
```

**OrderModel.java**
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order.model;

import cn.ntopic.order.dto.OrderDTO;
import cn.ntopic.order.enums.OrderStatusEnum;
import cn.ntopic.core.builder.ToString;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * 订单对象
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class OrderModel extends ToString {

    /**
     * 订单ID，非空
     */
    private String id;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单状态
     */
    private OrderStatusEnum status;

    /**
     * 构建订单模型
     */
    public static OrderModel from(OrderDTO orderDTO) {
        Assert.notNull(orderDTO, "OrderDTO入参为NULL.");

        OrderModel orderModel = new OrderModel();
        orderModel.setId(orderDTO.getId());
        orderModel.setAmount(new BigDecimal(orderDTO.getAmount()));
        orderModel.setStatus(OrderStatusEnum.convert(orderDTO.getStatus()).orElse(OrderStatusEnum.UNKNOWN));

        return orderModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }
}
```

**OrderStatusEnum.java**
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * 订单状态枚举
 *
 * @author obullxl 2023年05月20日: 新增
 */
public enum OrderStatusEnum {

    UNKNOWN(0, "未知"),

    WAIT_PAY(3, "创建待支付"),

    CLOSED(5, "支付超时关闭"),

    FINISHED(9, "订单已完成"),

    ;

    /**
     * 状态代码
     */
    private final int code;

    /**
     * 状态描述
     */
    private final String message;

    OrderStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 枚举转换
     */
    public static Optional<OrderStatusEnum> convert(int code) {
        return Arrays.stream(OrderStatusEnum.values()).filter(e -> code == e.getCode()).findFirst();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
```

***
