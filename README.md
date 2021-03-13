# sjutfleamarket.net
参加2021年上海市大学生计算机应用能力大赛的项目，已上线。
使用到的技术：springboot\mybatis\shiro\thymeleaf\mysql

## 数据库建表SQL语句
```sql
create database test;

-- 创建项目数据库
create database sjutfleamarket;
use sjutfleamarket;

-- 创建用户表
create table `user`(
	-- 主键id无符号自增
	`userid` int(7) unsigned auto_increment,
    `username` varchar(20) not null,
    `password` varchar(50) not null,
    `birthday` datetime not null default '1900-01-01 00:00:00',
    `university` varchar(30),
    `email` varchar(50) not null,
    `tele` varchar(20),
    `oci` varchar(500),
    -- 性别：男、女、保密
    `gender` varchar(5),
    primary key(`userid`)
)engine=InnoDB default charset=utf8;

-- 创建商品表
create table `goods`(
	-- 主键id无符号自增
    `goodsid` int(8) unsigned auto_increment,
    -- 外键userid
    `userid` int(7) unsigned,
    -- 联系属性
    `deliveryTime` datetime not null default '1900-01-01 00:00:00',
    `title` varchar(30) not null,
    `description` varchar(1000),
    -- 分类：
    -- 学习用品
    -- 交通工具
    -- 生活日用品
    -- 衣物
    -- 电子产品
    -- 书籍
    -- 其他
    `category` varchar(10) not null,
    `originalPrice` float not null,
    `price` float not null,
    -- 状态：0为下架，1为未下架
    `status` tinyint(1) not null,
    primary key(`goodsid`),
    foreign key(`userid`) references `user`(`userid`)
)engine=InnoDB default charset=utf8;

-- 创建图片表
create table `images`(
	`imgurl` varchar(200),
    `goodsid` int(8) unsigned,
    primary key(`imgurl`),
    foreign key(`goodsid`) references `goods`(`goodsid`)
)engine=InnoDB default charset=utf8;

-- 创建评论表
create table `comment`(
	`commentid` int(11) unsigned auto_increment,
    `userid` int(7) unsigned,
    `goodsid` int(8) unsigned,
    `commentDeliveryTime` datetime,
    `content` varchar(200),
    primary key(`commentid`),
    foreign key(`userid`) references `user`(`userid`),
    foreign key(`goodsid`) references `goods`(`goodsid`)
)engine=InnoDB default charset=utf8;

```

## 开发日志
### day01:
 0. 数据库在前几天已经设计实施好了
 1. 实现首页查看所有商品的功能
 2. 实现基本的页面跳转结构
 3. 实现根据商品种类分类查看商品信息的功能
 4. 实现关键字搜索数据库中的商品功能
 5. 解决前台分页查看数据问题
### day02:
 1. 整合shiro，实现用户登录、登出功能
 2. 实现用户注册功能
### day03:
 1. 编写后台管理页面
 2. 实现用户修改用户信息功能
 3. 实现用户注销账户功能
### day04:
 1. 实现商品的发布功能 
 2. 实现文件（图片）批量上传功能
### day05:
 1. 解决初次访问时地址栏出现;jsessionid=导致404的问题
 2. 实现商品图片在商品详情页的展示
 3. 实现商品详情页评论的展示
### day06:
 1. 完善请求拦截
 2. 实现用户查看自己发布的商品
### day07-10:
 1. 协同其他成员完成前端页面的开发
 2. 实现添加评论的功能
 3. 实现修改商品信息的功能
 4. 购买服务器与域名，完成服务器端项目运行环境与数据库的搭建
### day11:
 1. 实现商品的下架、上架和删除功能
 2. 修复用户注销功能
 3. 完成大部分页面数据的交接
### day12:
 1. 完善请求拦截
 2. 解决发布商品功能的一些bug
 3. 对前端页面进行一些修改
### day13:
 1. 对用户上传的图片进行压缩
 2. 打包部署
