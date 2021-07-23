package com.cloudwise.syslog4j.runable;

import com.cloudwise.syslog4j.properties.SyslogProperties;
import com.cloudwise.syslog4j.store.SyslogServerStore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfig;
import org.productivity.java.syslog4j.server.impl.net.udp.UDPNetSyslogServerConfig;

import java.util.Arrays;


/**
 * @author Aiden.Liu
 * @description
 * @date 2021-07-15 12:54
 */
@AllArgsConstructor
@NoArgsConstructor
public class SyslogRunable implements Runnable {

    SyslogProperties.SyslogConfig syslogConfig;

    SyslogServerConfigIF fillConfig(SyslogProperties.SyslogConfig syslogConfig) {
        SyslogServerConfigIF config;
        int port = syslogConfig.getPort();
        String host = syslogConfig.getHost();

        switch (syslogConfig.getProtocol()) {
            case TCP:
                config = new TCPNetSyslogServerConfig(host, port);
                break;
            case UDP:
            default:
                config = new UDPNetSyslogServerConfig(host, port);
                break;
        }
        return config;
    }

    @Override
    public void run() {
        SyslogServerConfigIF config = fillConfig(syslogConfig);
        String protocol = syslogConfig.getProtocol().getValue();
        if (null != SyslogServerStore.get(config)) {
            return;
        }
        String handlerClassStr = syslogConfig.getHandlerClass();
        if (null == handlerClassStr || "".equals(handlerClassStr)) {
            return;
        } else {
            try {
                Class<?> handlerClass = Class.forName(handlerClassStr);
                Object handler = handlerClass.newInstance();
                if (!(handler instanceof SyslogServerEventHandlerIF)) {
                    throw new RuntimeException("the handler must implements the interface that named SyslogServerEventHandlerIF");
                }
                config.addEventHandler((SyslogServerEventHandlerIF) handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SyslogServerIF syslogServer = execSyslogServer(config, protocol);
        SyslogServerStore.put(config, syslogServer);
    }

    private SyslogServerIF execSyslogServer(SyslogServerConfigIF config, String protocol) {
        SyslogServerIF syslogServer;
        try {
            Class syslogClass = config.getSyslogServerClass();
            syslogServer = (SyslogServerIF) syslogClass.newInstance();
        } catch (ClassCastException var7) {
            throw new SyslogRuntimeException(var7);
        } catch (IllegalAccessException var8) {
            throw new SyslogRuntimeException(var8);
        } catch (InstantiationException var9) {
            throw new SyslogRuntimeException(var9);
        }
        syslogServer.initialize(protocol, config);

        if (syslogServer.getThread() == null) {
            Thread thread = new Thread(syslogServer);
            thread.setName("SyslogServer: " + config.getHost() + ":" + config.getPort() + "-" + protocol);
            syslogServer.setThread(thread);
            thread.start();
        }
        return syslogServer;
    }
}
