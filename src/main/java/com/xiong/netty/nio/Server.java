package com.xiong.netty.nio;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        int port = 60000;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {
                logger.error("port is error", e);
            }
        }
        
        MultiplexerServer server = new MultiplexerServer(port);
        
        new  Thread(server,"NIO-MultiplexerServer-001").start();
    }

}
