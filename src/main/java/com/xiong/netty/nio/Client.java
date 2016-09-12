package com.xiong.netty.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Client {
    
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        int port = 60000;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {
                logger.error("port is error");
            }
        }
        
        new Thread(new ClientHandle("127.0.0.1",port)).start();
    }

}
