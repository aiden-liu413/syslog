package com.cloudwise.syslog4j.handler;

import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.springframework.context.ApplicationContext;

public interface SyslogServerhandler extends SyslogServerEventHandlerIF{
    /**
     * init method
     * @param applicationContext
     */
    default void init(ApplicationContext applicationContext){

    }

}
