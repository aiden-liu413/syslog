# syslog
本项目封装可syslog，让使用者只需要在配置文件中添加相关配置 即可开启syslogserver（服务端/接收端）
```yaml
cloudwise:
  syslog:
    #enable为false 则syslog配置不生效
    #enable: false
    server:
      - host: 0.0.0.0
        port: 1231
        protocol: UDP
      - host: 0.0.0.0
        port: 1121
        protocol: UDP
      - host: 0.0.0.0
        port: 7777
        protocol: UDP
```


