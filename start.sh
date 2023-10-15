#!/bin/sh

# 暂停Apache服务，释放80端口
sudo apachectl stop

# 在80端口上启动Hugo，调试页面
sudo hugo server -p 80 -D
