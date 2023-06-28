+++
slug = "2023062201"
date = "2023-06-22"
lastmod = "2023-06-22"
title = "使用Gitee或GitHub托管Maven仓库JAR包方法"
description = "我们开源了组件的源代码，希望有更多人能更便捷的使用组件，最好的办法是把组件的JAR包发布到Maven中央仓库，直接通过Maven/Gradle等方式快速引用使用……"
image = "01.png"
tags = [ "Maven", "Gitee仓库", "GitHub仓库" ]
categories = [ "专业技术", "开源组件" ]
+++

- Gitee托管仓库：[https://gitee.com/obullxl/maven-repository](https://gitee.com/obullxl/maven-repository)
- GitHub托管仓库：[https://github.com/obullxl/maven-repository](https://github.com/obullxl/maven-repository)

---

## 背景说明
在上一篇博客中，我们介绍了通用分布式id序列组件（[https://ntopic.cn/p/2023062101/](https://ntopic.cn/p/2023062101/)）的设计思路，并把源代码托管在了Gitee（[https://gitee.com/obullxl/sequence-jdbc](https://gitee.com/obullxl/sequence-jdbc)）和GitHub（[https://github.com/obullxl/sequence-jdbc](https://github.com/obullxl/sequence-jdbc)）。

我们希望能让更多人便捷的使用本组件，那么把JAR包放到到Maven官方的中心仓库（[https://mvnrepository.com](https://mvnrepository.com)）是最好的选择。

要把JAR包上传到Maven官方中心仓库，步骤比较繁琐，包括注册、申请、发布配置等一系列操作。其实我们的本意只是想把自己的开源项目打包让大家更方便使用，能否有更快捷的方式呢？当然是有的，就是使用Gitee或者GitHub进行托管，把他们作文JAR包的文件存储。

## Gitee/GitHub仓库设置
由于Gitee和GitHub原理完全一致，下面说明以Gitee为主。

建议在Gitee中单独申请一个仓库，专门用于存放JAR包，比如我的仓库叫maven-repository：[https://gitee.com/obullxl/maven-repository](https://gitee.com/obullxl/maven-repository)

同时，便于后续多个JAR包共用一个仓库，JAR包统一放到仓库的`repository`目录中，如分布式id组件分支为`sequence-jdbc`：

![](02.jpg)

## 打包发布JAR包到仓库
Gitee仓库分支设置好之后，开始设置我们打包并发布JAR包了。

### pom.xml打包配置
完整的配置可直接参考分布式id序列的设置：[https://gitee.com/obullxl/sequence-jdbc/blob/main/pom.xml](https://gitee.com/obullxl/sequence-jdbc/blob/main/pom.xml)

- pom.xml文件，一定需要定义`groupId`/`artifactId`/`version`这Maven依赖坐标三要素：

```xml
<groupId>cn.ntopic</groupId>
<artifactId>sequence-jdbc</artifactId>
<version>1.0.1</version>
<packaging>jar</packaging>
```

- pom.xml文件，配置build节点，指定JAR打包、Deploy发布的配置（先发布到本地目录，即项目根目录下`./repository/sequence-jdbc`目录）：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>8</source>
                <target>8</target>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-source-plugin</artifactId>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>jar</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-deploy-plugin</artifactId>
            <configuration>
                <!-- 关键配置：指定JAR包发布到本地目录，即项目根目录下./repository/sequence-jdbc目录 -->
                <altDeploymentRepository>internal.repo::default::file://${project.build.directory}/../repository/sequence-jdbc</altDeploymentRepository>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 打包并上传到仓库
- 打包并发布到本地目录命令：

```shell
mvn clean
mvn deploy -Dmaven.test.skip=true
```

- 上传到远程仓库命令：

```shell
cd ./repository/sequence-jdbc
git add *
git commit -m 'Deploy sequence-jdbc JAR: https://gitee.com/obullxl/sequence-jdbc'
git push origin master
```

完整的打包命令，请参考分布式id序列源仓库代码：[https://gitee.com/obullxl/sequence-jdbc/blob/main/maven.deploy.sh](https://gitee.com/obullxl/sequence-jdbc/blob/main/maven.deploy.sh)：

```shell
#!/bin/bash

# 创建目录
rm -rf ./repository
mkdir ./repository

# 远程准备
cd ./repository
git clone -b master https://gitee.com/obullxl/maven-repository.git sequence-jdbc

# 本地打包
cd ../
mvn clean
mvn deploy -Dmaven.test.skip=true

# 上传仓库
cd ./repository/sequence-jdbc
git add *
git commit -m 'Deploy sequence-jdbc JAR: https://gitee.com/obullxl/sequence-jdbc'
git push origin master

# 返回项目
cd ../../
```

## 其他项目使用JAR包方法
和Maven官方的中心仓库相比，Gitee托管仓库没有本质区别，只需要在pom.xml中配置Gitee的托管仓库即可，让Maven知道从哪儿去下载JAR包。

### pom.xml中增加仓库
`pom.xml`中增加Gitee托管仓库地址：

```xml
<repositories>
  <repository>
    <id>Gitee-obullxl</id>
    <url>https://gitee.com/obullxl/maven-repository/raw/master/repository</url>
  </repository>
</repositories>
```

### Maven配置依赖
和其他JAR包一样，`pom.xml`中增加依赖坐标：

```xml
<dependency>
    <groupId>cn.ntopic</groupId>
    <artifactId>sequence-jdbc</artifactId>
    <version>1.0.1</version>
</dependency>
```
