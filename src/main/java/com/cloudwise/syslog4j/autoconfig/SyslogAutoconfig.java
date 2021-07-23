package com.cloudwise.syslog4j.autoconfig;

import com.cloudwise.syslog4j.condition.OnAnnotationCondition;
import com.cloudwise.syslog4j.properties.SyslogProperties;
import com.cloudwise.syslog4j.runable.SyslogRunable;
import lombok.extern.java.Log;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Aiden.Liu
 * @description syslog自动配置
 * @date 2021-07-15 12:48
 */
@ConditionalOnProperty(value = "cloudwise.syslog.enable", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties({SyslogProperties.class})
//@Conditional({OnAnnotationCondition.class})
@Log
public class SyslogAutoconfig implements InitializingBean {

    @Autowired
    SyslogProperties syslogProperties;


    @Override
    public void afterPropertiesSet() {
        AnnotatedTypeMetadata metadata;
        if (Objects.isNull(syslogProperties) || Objects.isNull(syslogProperties.getServer())) {
            log.info(">>>>>syslogProperties is null, skipped the configure of syslog");
            return;
        }
        List<SyslogProperties.SyslogConfig> serverList = syslogProperties.getServer();
        serverList = serverList.stream()
                .filter(p -> StringUtils.hasText(p.getHandlerClass())).distinct()
                .collect(Collectors.toList());
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int i = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
        executor.setCorePoolSize(2 * i);//核心池大小
        executor.setMaxPoolSize(100);//最大线程数
        executor.setQueueCapacity(1000);//队列程度
        executor.setKeepAliveSeconds(1000);//线程空闲时间
        executor.setThreadNamePrefix("syslog-therd");//线程前缀名称
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());//配置拒绝策略
        executor.initialize();
        serverList.forEach(k -> {
            executor.execute(new SyslogRunable(k));
        });
        log.info(">>>>>syslog启动");
        log.info(">>>>>有效启动参数：" + serverList);
    }
}
