## tomcat community

## 资料
[Spring 文档](https://spring.io/guides)  
[Sprint Web](https://spring.io/guides/gs/serving-web-content/)  
[ES 社区](https://elasticsearch.cn/explore)  
[BootStrap 文档](https://v3.bootcss.com/components/)  
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
[Spring 内置数据库](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-embedded-database-support)  


## 工具
[Git](https://git-scm.com/download)  
[Visual Paradigm](https://www.visual-paradigm.com)  

## 其他

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