/**
 * 创建数据库
 */
CREATE DATABASE web_crwal DEFAULT CHARSET=UTF8;
 
/**
 * 创建用户和授权
 */
GRANT ALL PRIVILEGES ON web_crwal.* TO 'crwal'@'%' IDENTIFIED BY 'crwal';
GRANT ALL PRIVILEGES ON web_crwal.* TO 'crwal'@'localhost' IDENTIFIED BY 'crwal';
