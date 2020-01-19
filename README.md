## tomcat community

## 资料
[Spring 文档](https://spring.io/guides)  
[Sprint Web](https://spring.io/guides/gs/serving-web-content/)  
[ES 社区](https://elasticsearch.cn/explore)  
[BootStrap 文档](https://v3.bootcss.com/components/)  
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
[Spring 内置数据库](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-embedded-database-support)  
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)

## 工具
[Git](https://git-scm.com/download)  
[Visual Paradigm](https://www.visual-paradigm.com)  
[Flyway](https://flywaydb.org/getstarted/firststeps/maven)  
[Lombok](https://projectlombok.org/)

## 其他
执行Flyway脚本  
mvn flyway:migrate

## 脚本
```sql
create table USER
(
    ID           BIGINT auto_increment primary key,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        VARCHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    BIO          VARCHAR(256),
    AVATAR_URL   VARCHAR(100)
);
```

## 答疑
1、textarea 使用 th:value 不能回显
2、fastjson 可以自动把下划线标示映射到驼峰的属性
3、h2数据库到底是什么和链接异常的处理
4、错误提示没有的时候发布按钮飘动到了左边
5、列表中日期格式化问题
6、idea 默认没有安装 lombok 插件
7、提效 chrome 插件介绍