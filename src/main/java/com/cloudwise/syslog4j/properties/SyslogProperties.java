package com.cloudwise.syslog4j.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Aiden.Liu
 * @description syslog配置文件
 * @date 2021-07-15 10:25
 */
@ConfigurationProperties(prefix = "cloudwise.syslog")
@Data
public class SyslogProperties {
    List<SyslogConfig> server;

    @Getter
    public enum Protocol{
        UDP("udp"),TCP("tcp");
        String value;

        Protocol(String value) {
            this.value = value;
        }
    }

    @Data
    public static class SyslogConfig {
        String host;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SyslogConfig that = (SyslogConfig) o;

            if (port != that.port) return false;
            return host != null ? host.equals(that.host) : that.host == null;
        }

        @Override
        public int hashCode() {
            int result = host != null ? host.hashCode() : 0;
            result = 31 * result + port;
            return result;
        }

        int port;
        Protocol protocol;
        String handlerClass;
    }
}
