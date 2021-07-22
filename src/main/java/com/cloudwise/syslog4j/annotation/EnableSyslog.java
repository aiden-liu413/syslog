package com.cloudwise.syslog4j.annotation;

import com.cloudwise.syslog4j.autoconfig.SyslogAutoconfig;
import com.cloudwise.syslog4j.properties.SyslogProperties;
import org.productivity.java.syslog4j.server.SyslogServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Aiden.Liu
 * @description syslog自动配置注解,
 * @date 2021-07-15 12:42
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties(SyslogProperties.class)
@ConditionalOnClass(SyslogServer.class)
@Import({SyslogAutoconfig.class})
public @interface EnableSyslog {
}
