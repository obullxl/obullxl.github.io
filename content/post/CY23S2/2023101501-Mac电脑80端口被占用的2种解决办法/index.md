+++
slug = "2023101501"
date = "2023-10-15"
lastmod = "2023-10-15"
title = "Mac电脑80端口被占用的2种解决办法"
description = "我们在调试Web页面时，期望能使用80端口调试（比如本博客站点的每篇博客，我在发布之前，均需要在我的Mac电脑进行博客内容和样式的调试和校验）。而Mac电脑的80端口默认被系统Apache服务占用，下面分享我常用的2种使用80端口调试Web站点页面的办法……"
image = "00.jpg"
tags = [ "Apache", "端口" ]
categories = [ "专业技术" ]
+++

## 前提：确认占用80端口的程序（sudo lsof -i:80）
我研发用的是<b>Mac电脑</b>，查看<b>80端口</b>被占用的进程命令：`sudo lsof -i:80`

我Mac电脑80端口占用进程如下：<b>httpd进程</b>占用了80端口，httpd进程即为<b>Apache服务</b>，下面提供了<b>2种</b>解决办法。

```shell
OXL-MacBook:~ obullxl$ sudo lsof -i:80
COMMAND  PID    USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
httpd    128    root    4u  IPv6 0x837e41eb989fc6d1      0t0  TCP *:http (LISTEN)
WeChat   390 obullxl  103u  IPv4 0x837e41e6ce1a65d9      0t0  TCP 192.168.101.30:49216->182.50.15.211:http (CLOSE_WAIT)
WeChat   390 obullxl  106u  IPv4 0x837e41e6ce1a3089      0t0  TCP 192.168.101.30:49217->182.50.10.149:http (CLOSE_WAIT)
WeChat   390 obullxl  342u  IPv4 0x837e41e6ce6045d9      0t0  TCP 192.168.101.30:52992->61.241.138.140:http (CLOSE_WAIT)
httpd    616    _www    4u  IPv6 0x837e41eb989fc6d1      0t0  TCP *:http (LISTEN)
httpd   4646    _www    4u  IPv6 0x837e41eb989fc6d1      0t0  TCP *:http (LISTEN)
httpd   4647    _www    4u  IPv6 0x837e41eb989fc6d1      0t0  TCP *:http (LISTEN)
httpd   4648    _www    4u  IPv6 0x837e41eb989fc6d1      0t0  TCP *:http (LISTEN)
```

## 方案一：暂停Apache系统服务后，启动Web调试
<b>Apache服务</b>启动和暂停，可以使用<b>apachectl</b>命令行：`sudo apachectl restart`和`sudo apachectl stop`

我的电脑暂停了Apache服务之后，再次查看<b>80端口</b>已经没有占用了：

```shell
OXL-MacBook:~ obullxl$ sudo apachectl stop
OXL-MacBook:~ obullxl$ sudo lsof -i:80
OXL-MacBook:~ obullxl$
```

为了让我们调试Web站点页面更加顺畅，我们可以把Apache暂停命令加到启动Web调试的命令中，如Hugo静态站点的脚本：

```shell
#!/bin/sh

# 暂停Apache服务，释放80端口
sudo apachectl stop

# 在80端口上启动Hugo，调试页面
sudo hugo server -p 80 -D
```

本方案的<b>唯一不足</b>就是：当我们使用`control + C`暂停了Hugo服务，释放了80端口后，Apache服务不能<b>自动启动</b>。需要手工执行命令重启Apache服务，优化方案请看<b>方案二</b>。

## 方案二：修改Apache系统服务80端口为其他端口
方案一可以解决我们在80端口调试Web页面，但是在调试完成之后，Apache服务需要手工执行命令重启。本方案就是修改Apache服务默认的80端口为其他端口，让出80端口给我们业务页面使用。

- 第一步 找到Apache的安装目录：我Mac电脑目录是`/etc/apache2`
```shell
OXL-MacBook:~ obullxl$ cd /etc
OXL-MacBook:etc obullxl$ ls | grep apache
apache2
OXL-MacBook:etc obullxl$ cd apache2/
OXL-MacBook:apache2 obullxl$ ls
extra     magic     other     httpd.conf    httpd.conf.pre-update mime.types    users
httpd.conf~previous original
```

 - 第二步 修改Apache配置文件（`httpd.conf`）中80端口为其他端口，如<b>8080</b>端口。为了安全起见，修改配置文件之前，先进行备份。

```shell
OXL-MacBook:apache2 obullxl$ sudo cp httpd.conf httpd.conf.20231015
Password:
OXL-MacBook:apache2 obullxl$ sudo vi httpd.conf
```

```xml
<IfDefine SERVER_APP_HAS_DEFAULT_PORTS>
    Listen 8080
</IfDefine>
<IfDefine !SERVER_APP_HAS_DEFAULT_PORTS>
    Listen 8080
</IfDefine>
```

 - 第三步 重启Apache服务：`sudo apachectl restart`，同时可以查看8080端口，已经被Apache服务占用。

```shell
OXL-MacBook:apache2 obullxl$ sudo apachectl restart
OXL-MacBook:apache2 obullxl$ sudo lsof -i:8080
COMMAND  PID USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
httpd   6942 root    4u  IPv6 0x837e41eb989f9cd1      0t0  TCP *:http-alt (LISTEN)
httpd   6955 _www    4u  IPv6 0x837e41eb989f9cd1      0t0  TCP *:http-alt (LISTEN)
```

## 最后
方案一和方案二个人验证均可行~

---
我的本博客原地址：[https://ntopic.cn/p/2023101501](https://ntopic.cn/p/2023101501/)

---
