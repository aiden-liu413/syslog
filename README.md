# syslog
syslog-spring-boot-starter
本项目封装syslog，让使用者只需要在配置文件中添加相关配置 即可开启syslogserver（服务端/接收端）

- maven依赖
```pom
        <dependency>
            <groupId>com.cloudwise</groupId>
            <artifactId>syslog-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
- 配置文件
```yaml
cloudwise:
  syslog:
    #enable: false
    server:
      - host: 0.0.0.0
        port: 1231
        protocol: UDP
        handlerClass: org.productivity.java.syslog4j.server.impl.event.printstream.SystemErrSyslogServerEventHandler
      - host: 0.0.0.0
        port: 1121
        protocol: UDP
        handlerClass: org.productivity.java.syslog4j.server.impl.event.printstream.SystemErrSyslogServerEventHandler
      - host: 0.0.0.0
        port: 7777
        protocol: UDP
        handlerClass: org.productivity.java.syslog4j.server.impl.event.printstream.SystemErrSyslogServerEventHandler

```
- 启动示例
![图片](https://user-images.githubusercontent.com/44597411/126607211-bfdba270-9de1-4e7b-ad26-ec75747a6cf6.png)

![图片](https://user-images.githubusercontent.com/44597411/126607104-1e5e3620-2053-4efe-9437-65d08238f5a6.png)
