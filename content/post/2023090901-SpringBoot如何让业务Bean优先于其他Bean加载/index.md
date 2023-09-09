+++
slug = "2023090901"
date = "2023-09-09"
lastmod = "2023-09-09"
title = "SpringBoot如何让业务Bean优先于其他Bean加载"
description = "SpringBoot项目的业务工具类（如：参数工具类ParamUtils，仅包含static方法，依赖DAO访问DB加载数据），在SpringBoot启动过程中会被其他业务Bean初始化依赖。由于参数工具类和业务Bean均被Spring框架托管，如何在其他Bean初始化之前，就优雅安全的初始化ParamUtils就至关重要了……"
image = "00-01.jpg"
#image = "https://picsum.photos/id/56/2000/400.jpg"
tags = [ "SpringBoot", "Bean依赖" ]
categories = [ "专业技术" ]
+++

源代码先行：
- Gitee本文介绍的完整仓库：[https://gitee.com/obullxl/ntopic-boot](https://gitee.com/obullxl/ntopic-boot)
- GitHub本文介绍的完整仓库：[https://github.com/obullxl/ntopic-boot](https://github.com/obullxl/ntopic-boot)

## 背景介绍
今天走读一个应用程序代码，发现一个有趣的现象：有多个不同的业务Bean中均依赖了一个参数工具类ParamUtils（即：`@Autowired ParamUtils paramUtis`），ParamUtils依赖了`ParamDAO Bean`用于从DB中获取参数；为了便于ParamUtils使用，工具类全部都是static静态方法，也就是说，业务Bean`仅仅增加Autowired依赖`，在实际调用时还是直接`使用的ParamUtils类静态方法`。那个Autowired注入ParamUtils的依赖看起来是无用代码，但是其实还`不能`去掉。

代码业务这么写的目的其实很好理解：因为ParamUtils依赖了DAO Bean，增加依赖是保障ParamUtils的类静态方法在调用时已经被SpringBoot初始化了。那么，有没有更优雅的办法，能让业务代码更优雅更安全的使用ParamUtils工具类呢？

## 思路分析
ParamUtils业务Bean，比其他的业务Bean提前初始化，基本思路如下：

第一思路：采用优先级Ordered注解（类：org.springframework.core.Ordered），但是`不可行`，因为该注解主要是用于控制Spring自身Bean的初始化顺序，如Listener/Filter等。

第二思路：采用Bean依赖DependsOn注解（类：org.springframework.context.annotation.DependsOn），该方法`可行`，它和Autowired注解一致，也是表明Bean之间依赖，但是没有从本质上解决问题。

第三思路：手工注册Bean让Spring优先初始化，查看SpringApplication类代码，发现里面有个`addInitializers(ApplicationContextInitializer<?>... initializers)`方法，可以让业务在ApplicationContext初始化时`initialize(C applicationContext)`基于Context做一些事情。那么可不可以在这个地方，能`手工注册`业务Bean呢？

## 代码实现和验证
代码分为3部分：ParamDAO业务Bean访问DB，ParamUtils参数工具类依赖ParamDAO，RestController测试类使用参数工具类。

为了阅读方便，以下展示的代码均只有主体部分，完整的代码注释和代码内容，请下载本工程仓库。

### ParamDAO业务Bean
为了测试简便，本工程不依赖MySQL数据库，我们还是采用SQLite，源文件就在代码根目录下，clone本仓库后即可执行运行：

#### SQLite数据表准备
首先新建一张参数表（`nt_param`），并且插入一些数据。为了尽快验证我们的思路，其他的数据新增、修改和删除等就不做特别的验证了。

```sql
--
-- 参数表
--
CREATE TABLE nt_param
(
    id          bigint unsigned NOT NULL auto_increment,
    category    varchar(64) NOT NULL,
    module      varchar(64) NOT NULL,
    name        varchar(64) NOT NULL,
    content     varchar(4096) DEFAULT '',
    create_time timestamp,
    modify_time timestamp,
    PRIMARY KEY (id),
    UNIQUE (category, module, name)
);

--
-- 插入数据
--
INSERT INTO nt_param (category, module, name, content, create_time, modify_time)
VALUES ('CONFIG', 'USER', 'minAge', '18', strftime('%Y-%m-%d %H:%M:%f', 'now'), strftime('%Y-%m-%d %H:%M:%f', 'now')),
       ('CONFIG', 'USER', 'maxAge', '60', strftime('%Y-%m-%d %H:%M:%f', 'now'), strftime('%Y-%m-%d %H:%M:%f', 'now'));
```

#### ParamDAO数据查询
`NTParamDAO`为普通的Spring Bean（ID为：`ntParamDAO`）

```java
@Repository("ntParamDAO")
public interface NTParamDAO {

    @Select("SELECT * FROM nt_param WHERE category=#{category,jdbcType=VARCHAR} AND module=#{module,jdbcType=VARCHAR}")
    List<NTParamDO> selectByModule(@Param("category") String category, @Param("module") String module);

}
```

### ParamUtils工具类定义和使用
#### ParamUtils工具类定义：非Spring Bean
`ParamUtils`是静态工具类，依赖了ParamDAO Spring Bean，并且ParamUtils并`不是`Spring Bean：

```java
// @Component("ntParamUtils") SpringBoot优先初始化本类，因此无需增加注解
public class NTParamUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.DAS);

    /**
     * 系统参数DAO
     */
    private static NTParamDAO NT_PARAM_DAO;

    /**
     * 依赖注入
     */
    public NTParamUtils(@Qualifier("ntParamDAO") NTParamDAO ntParamDAO) {
        Assert.notNull(ntParamDAO, "NTParamDAO注入为NULL.");
        NT_PARAM_DAO = ntParamDAO;

        // 打印日志
        LOGGER.info("{}:初始化完成.", this.getClass().getName());
    }

    public static List<NTParamDO> findList(String category, String module) {
        Assert.hasText(category, "分类参数为空");
        Assert.hasText(module, "模块参数为空");
        return NT_PARAM_DAO.selectByModule(category, module);
    }

}
```

#### ParamUtils工具类使用：普通Spring Bean
`NTUserServiceImpl`是一个普通的Spring Bean，它`没有显示依赖`ParamUtils，而是直接使用它：

```java
@Component("ntUserService")
public final class NTUserServiceImpl implements NTUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.BIZ);

    @Autowired
    public NTUserServiceImpl() {
        // 打印日志
        LOGGER.info("{}:初始化完成.", this.getClass().getName());
    }

    /**
     * 获取用户模块参数
     */
    @Override
    public List<NTParamDO> findUserParamList() {
        return NTParamUtils.findList("CONFIG", "USER");
    }
}
```

### SpringBoot优先初始化设置
两个关键点：
1. ApplicationContextInitializer类：提供Context初始化入口，业务逻辑可以通过此次注入。
2. BeanDefinitionRegistryPostProcessor类：Spring Bean收集完成后，但还没有初始化之前入口，我们的`关键`就在这里定义ParamUtils Bean，并且Bean定义为`RootBeanDefinition`保障提前初始化。

#### Context自定义初始化：手工注册ParamUtils Bean
```java
public class NTApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, BeanDefinitionRegistryPostProcessor {
    
    /**
     * Context初始化，给业务逻辑初始化提供了机会
     */
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        // 注册Bean上下文初始化后处理器，用于手工注册Bean
        context.addBeanFactoryPostProcessor(this);
    }

    /**
     * 手工注册ParamUtils工具类，并且是RootBean定义，保障优先初始化，下面会详细分析
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 在ConfigurationClassPostProcessor前手动注册Bean，保障优先于其他Bean初始化
        registry.registerBeanDefinition("ntParamUtils", new RootBeanDefinition(NTParamUtils.class));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
```

#### SpringBoot启动类增加自定义初始化器
原来的方法：<u>SpringApplication.run(NTBootApplication.class, args);</u>

```java
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = "cn.ntopic.das..**.dao", sqlSessionFactoryRef = "ntSqlSessionFactory")
public class NTBootApplication {

    /**
     * SpringBoot启动
     */
    public static void main(String[] args) {
        // 注册自定义处理器
        SpringApplication application = new SpringApplication(NTBootApplication.class);
        application.addInitializers(new NTApplicationContextInitializer());

        // SpringBoot启动
        application.run(args);
    }
}
```

至此，业务Bean提前初始化的整个代码完毕，下面进行验证！

### ParamUtils初始化验证（符合预期）
我们分表从SpringBoot的`启动日志`和`实际使用`2个方面来验证我们的设计思路：

#### SpringBoot启动日志：符合预期
从`第21行`和`第22行`日志，可以看到，ParamUtils优于其他Bean完成初始化：

```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.5.3)

2023-09-09 11:40:55,607  INFO (StartupInfoLogger.java:55)- Starting NTBootApplication using Java 1.8.0_281 on OXL-MacBook.local with PID 1371 (/Users/obullxl/CodeSpace/ntopic-boot/ntopic/target/classes started by obullxl in /Users/obullxl/CodeSpace/ntopic-boot)
2023-09-09 11:40:55,612  INFO (SpringApplication.java:659)- No active profile set, falling back to default profiles: default
2023-09-09 11:40:55,692  INFO (DeferredLog.java:255)- Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2023-09-09 11:40:55,693  INFO (DeferredLog.java:255)- For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2023-09-09 11:40:56,834  INFO (TomcatWebServer.java:108)- Tomcat initialized with port(s): 8088 (http)
2023-09-09 11:40:56,842  INFO (DirectJDKLog.java:173)- Initializing ProtocolHandler ["http-nio-8088"]
2023-09-09 11:40:56,842  INFO (DirectJDKLog.java:173)- Starting service [Tomcat]
2023-09-09 11:40:56,842  INFO (DirectJDKLog.java:173)- Starting Servlet engine: [Apache Tomcat/9.0.50]
2023-09-09 11:40:56,901  INFO (DirectJDKLog.java:173)- Initializing Spring embedded WebApplicationContext
2023-09-09 11:40:56,901  INFO (ServletWebServerApplicationContext.java:290)- Root WebApplicationContext: initialization completed in 1208 ms
2023-09-09 11:40:57,043 ERROR (Log4j2Impl.java:58)- testWhileIdle is true, validationQuery not set
2023-09-09 11:40:57,051  INFO (Log4j2Impl.java:106)- {dataSource-1} inited
2023-09-09 11:40:57,127  INFO (NTParamUtils.java:39)- cn.ntopic.NTParamUtils:初始化完成.
2023-09-09 11:40:57,160  INFO (NTUserServiceImpl.java:78)- cn.ntopic.service.impl.NTUserServiceImpl:初始化完成.
2023-09-09 11:40:57,170  INFO (NTExecutorConfig.java:65)- start ntThreadPool
2023-09-09 11:40:57,563  INFO (OptionalLiveReloadServer.java:58)- LiveReload server is running on port 35729
2023-09-09 11:40:57,582  INFO (DirectJDKLog.java:173)- Starting ProtocolHandler ["http-nio-8088"]
2023-09-09 11:40:57,600  INFO (TomcatWebServer.java:220)- Tomcat started on port(s): 8088 (http) with context path ''
2023-09-09 11:40:57,610  INFO (StartupInfoLogger.java:61)- Started NTBootApplication in 2.363 seconds (JVM running for 3.091)
```

#### RestController验证：符合预期

```java
@RestController
public class NTParamAct {

    private final NTUserService ntUserService;

    public NTParamAct(@Qualifier("ntUserService") NTUserService ntUserService) {
        this.ntUserService = ntUserService;
    }

    @RequestMapping("/param")
    public List<NTParamDO> paramList() {
        return this.ntUserService.findUserParamList();
    }

}
```

打开浏览器，访问：[http://localhost:8088/param](http://localhost:8088/param)

可以看到，参数数据被查询并输出：
```json
[
    {
        "id": 3,
        "category": "CONFIG",
        "module": "USER",
        "name": "maxAge",
        "content": "60",
        "createTime": "2023-09-08T18:30:20.818+00:00",
        "modifyTime": "2023-09-08T18:30:20.818+00:00"
    },
    {
        "id": 2,
        "category": "CONFIG",
        "module": "USER",
        "name": "minAge",
        "content": "18",
        "createTime": "2023-09-08T18:30:20.818+00:00",
        "modifyTime": "2023-09-08T18:30:20.818+00:00"
    }
]
```

## SpringBoot实现分析
SpringBoot启动的代码入口：

```java
public static void main(String[] args) {
    // 注册自定义处理器
    SpringApplication application = new SpringApplication(NTBootApplication.class);
    application.addInitializers(new NTApplicationContextInitializer());

    // SpringBoot启动
    application.run(args);
}
```

有几个非常核心的点，基本调用链路：
1. SpringApplication类：run() -> prepareContext() -> applyInitializers(本方法：调用自定义`NTApplicationContextInitializer`上下文器)
2. SpringApplication类：run() -> refreshContext() -> refresh(ConfigurableApplicationContext)
3. ConfigurableApplicationContext类：AbstractApplicationContext.refresh() -> `finishBeanFactoryInitialization(ConfigurableListableBeanFactory)`
4. ConfigurableListableBeanFactory类，`关键代码`都在这里：`preInstantiateSingletons()`
 - beanDefinitionNames属性：Spring收集到的所有Bean定义，包括Repository注解、Component注解和我们手工定义的Bean
 - 遍历beanDefinitionNames的时候，优先RootBeanDefinition初始化，手工定义的ParamUtils也是该类型

至此，问题解决，能解决的原因也搞清楚了！
