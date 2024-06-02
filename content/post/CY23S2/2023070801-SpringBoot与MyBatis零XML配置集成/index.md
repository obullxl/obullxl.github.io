+++
slug = "2023070801"
date = "2023-07-08"
lastmod = "2023-07-08"
title = "SpringBoot与MyBatis零XML配置集成和集成测试"
description = "Java存在很多ORM框架，MyBaits框架是我们项目中使用得最多也是最愿意推荐的框架，它既有数据表和Java对象映射功能，又有原生SQL的特性。在与SpringBoot集成上，和其他框架一样，可以做到全注解化，无XML配置……"
image = "01.jpg"
#image = "https://picsum.photos/id/56/2000/400.jpg"
tags = [ "SpringBoot", "MyBatis" ]
categories = [ "专业技术" ]
+++

源代码先行：
- Gitee本文介绍的完整仓库：[https://gitee.com/obullxl/ntopic-boot](https://gitee.com/obullxl/ntopic-boot)
- GitHub本文介绍的完整仓库：[https://github.com/obullxl/ntopic-boot](https://github.com/obullxl/ntopic-boot)

## 背景介绍
在Java众多的ORM框架里面，MyBatis是比较轻量级框架之一，既有数据表和Java对象映射功能，在SQL编写方面又不失原生SQL的特性。SpringBoot提倡使用注解代替XML配置，同样的，在集成MyBatis时也可以做到全注解化，无XML配置。相关的集成方法网上存在大量的教程，本文是个人在实际项目过程的一个备忘，并不是复制和粘贴，同时在本文后面提供了完整的集成测试用例。

## MyBatis集成
涉及到以下4个方面：
- 我们Maven工程是一个SpringBoot工程
- 引入MyBatis的Starter依赖
- SpringBoot工程配置中增加MyBatis的配置
- Mapper/DAO通过注解实现

### SpringBoot工程依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.3</version>
        <relativePath/>
    </parent>

  <!-- 其他部分省略 -->
</project>
```

### MyBatis Starter依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!-- 其他省略 -->
      <dependencyManagement>
        <dependencies>
          <!-- MyBatis -->
          <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>2.2.0</version>
            </dependency>

          <!-- MySQL -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.26</version>
            </dependency>

          <!-- SQLite -->
            <dependency>
                <groupId>org.xerial</groupId>
                <artifactId>sqlite-jdbc</artifactId>
                <version>3.39.2.0</version>
                <scope>provided</scope>
            </dependency>

            <!-- Druid -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>1.1.2</version>
            </dependency>
          
          <!-- 其他省略 -->
          
        </dependencies>
      </dependencyManagement>
  <!-- 其他省略 -->
</project>
```

### SpringBoot Main配置
+ application.properties配置，增加DB数据源：
```properties
#
# 数据库：url值设置成自己的文件路径
#
ntopic.datasource.driver-class-name=org.sqlite.JDBC
ntopic.datasource.url=jdbc:sqlite:./../NTopicBoot.sqlite
ntopic.datasource.userName=
ntopic.datasource.password=
```

+ `MapperScan`注解：指明MyBatis的Mapper在哪写包中，`cn.ntopic.das..**.dao`指明，我们的Mapper类所在的包
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * NTopic启动器
 *
 * @author obullxl 2021年06月05日: 新增
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.ntopic.das..**.dao", sqlSessionFactoryRef = "ntSqlSessionFactory")
public class NTBootApplication {

    /**
     * SpringBoot启动
     */
    public static void main(String[] args) {
        SpringApplication.run(NTBootApplication.class, args);
    }

    /**
     * DataSource配置
     */
    @Bean(name = "ntDataSource", initMethod = "init", destroyMethod = "close")
    public DruidDataSource ntDataSource(@Value("${ntopic.datasource.url}") String url
            , @Value("${ntopic.datasource.userName}") String userName
            , @Value("${ntopic.datasource.password}") String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        dataSource.setInitialSize(0);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(5);
        dataSource.setMaxWait(3000L);

        return dataSource;
    }

    /**
     * MyBatis事务配置
     */
    @Bean("ntSqlSessionFactory")
    public SqlSessionFactoryBean ntSqlSessionFactory(@Qualifier("ntDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean;
    }

    /** 其他代码省略 */
}
```

### MyBatis Mapper类/DAO类
几个核心的注解：
- Insert插入
- Select查询
- Update更新
- Delete删除
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.das.dao;

import cn.ntopic.das.model.UserBaseDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author obullxl 2021年10月17日: 新增
 */
@Repository("userBaseDAO")
public interface UserBaseDAO {

    /**
     * 新增用户记录
     */
    @Insert("INSERT INTO nt_user_base (id, name, password, role_list, ext_map, create_time, modify_time)" +
            " VALUES (#{id,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, #{roleList,jdbcType=VARCHAR}, #{extMap,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, #{modifyTime,jdbcType=TIMESTAMP})")
    void insert(UserBaseDO userBaseDO);

    /**
     * 根据ID查询记录
     */
    @Select("SELECT * FROM nt_user_base WHERE id = #{id,jdbcType=VARCHAR}")
    UserBaseDO selectById(@Param("id") String id);

    /**
     * 根据名称查询记录
     */
    @Select("SELECT * FROM nt_user_base WHERE name = #{name,jdbcType=VARCHAR}")
    UserBaseDO selectByName(@Param("name") String name);

    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM nt_user_base LIMIT 30")
    List<UserBaseDO> select();

    /**
     * 更新角色列表
     * FIXME: SQLite/MySQL 当前时间函数不一致，本次通过入参传入！
     */
    @Update("UPDATE nt_user_base SET modify_time=#{modifyTime,jdbcType=TIMESTAMP}, role_list=#{roleList,jdbcType=VARCHAR} WHERE id=#{id,jdbcType=VARCHAR}")
    int updateRoleList(@Param("id") String id, @Param("modifyTime") Date modifyTime, @Param("roleList") String roleList);

    /**
     * 删除用户记录
     */
    @Delete("DELETE FROM nt_user_base WHERE id = #{id,jdbcType=VARCHAR}")
    int deleteById(@Param("id") String id);

    /**
     * 删除用户记录
     */
    @Delete("DELETE FROM nt_user_base WHERE name = #{name,jdbcType=VARCHAR}")
    int deleteByName(@Param("name") String name);

}
```

## 集成测试（包括CRUD操作）
```java
/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.das.dao;

import cn.ntopic.das.model.UserBaseDO;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author obullxl 2021年10月17日: 新增
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserBaseDAOTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    @Qualifier("userBaseDAO")
    private UserBaseDAO userBaseDAO;

    /**
     * CRUD简单测试
     */
    @Test
    public void test_insert_select_update_delete() {
        final String id = "ID-" + RandomUtils.nextLong();
        final String name = "NAME-" + RandomUtils.nextLong();

        // 1. 清理数据
        this.userBaseDAO.deleteById(id);
        this.userBaseDAO.deleteByName(name);

        // 请求数据 - 验证
        UserBaseDO userBaseDO = this.userBaseDAO.selectById(id);
        Assert.assertNull(userBaseDO);

        userBaseDO = this.userBaseDAO.selectByName(name);
        Assert.assertNull(userBaseDO);

        try {
            // 2. 新增数据
            UserBaseDO newUserDO = new UserBaseDO();
            newUserDO.setId(id);
            newUserDO.setName(name);
            newUserDO.setCreateTime(new Date());
            newUserDO.setModifyTime(new Date());
            this.userBaseDAO.insert(newUserDO);

            // 3. 数据查询 - 新增验证
            UserBaseDO checkUserDO = this.userBaseDAO.selectById(id);
            Assert.assertNotNull(checkUserDO);
            Assert.assertEquals(name, checkUserDO.getName());
            Assert.assertNotNull(checkUserDO.getCreateTime());
            Assert.assertNotNull(checkUserDO.getModifyTime());
            Assert.assertTrue(StringUtils.isBlank(checkUserDO.getRoleList()));

            // 4. 更新数据
            int update = this.userBaseDAO.updateRoleList(id, new Date(), "ADMIN,DATA");
            Assert.assertEquals(1, update);

            // 更新数据 - 验证
            checkUserDO = this.userBaseDAO.selectById(id);
            Assert.assertNotNull(checkUserDO);
            Assert.assertEquals("ADMIN,DATA", checkUserDO.getRoleList());

            // 5. 数据删除
            int delete = this.userBaseDAO.deleteById(id);
            Assert.assertEquals(1, delete);

            delete = this.userBaseDAO.deleteByName(name);
            Assert.assertEquals(0, delete);
        } finally {
            // 清理数据
            this.userBaseDAO.deleteById(id);
        }
    }
}
```

---

关注本公众号，我们共同学习进步 👇🏻👇🏻👇🏻

![微信公众号：老牛同学](https://ntopic.cn/WX-21.png)

---

我的本博客原地址：[https://mp.weixin.qq.com/s/JpvF9PfihYDZffPRKMDWwg](https://mp.weixin.qq.com/s/JpvF9PfihYDZffPRKMDWwg)

---
