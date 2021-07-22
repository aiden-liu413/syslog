package com.cloudwise.syslog4j.store;



import org.productivity.java.syslog4j.impl.net.tcp.TCPNetSyslogConfig;
import org.productivity.java.syslog4j.impl.net.udp.UDPNetSyslogConfig;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Aiden.Liu
 * @description
 * @date 2021-07-16 18:11
 */
public class SyslogServerStore {
    private static Map instances = new ConcurrentHashMap(16);

    public static void put(SyslogServerConfigIF config, SyslogServerIF syslogServer) {
        String key = grantKey(config);
        instances.put(key, syslogServer);
    }

    protected static String grantKey(SyslogServerConfigIF config) {
        String key = config.getHost() + "-" + config.getPort() + "-";
        if (config instanceof UDPNetSyslogConfig) {
            key = key + "udp";
        } else if (config instanceof TCPNetSyslogConfig) {
            key = key + "tcp";
        }
        return key;
    }

    public static SyslogServerIF get(SyslogServerConfigIF config) {
        String key = grantKey(config);
        return (SyslogServerIF) instances.get(key);
    }
}
