package com.xiong.netty.pio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类Server.java的实现描述：BIO服务端
 * @author hzxiongshiqiang 2016年9月2日 下午3:37:55
 */
public class Server {
    
   private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        int port = 60000;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {
                logger.error("port is error",e);
            }
        }
        
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            logger.info("server is start in port "+port);
            Socket socket = null;
            ServerExecutePool pool = new ServerExecutePool(5, 100);
            while(true){
                socket = serverSocket.accept();
                pool.execute(new ServerHandler(socket));
            }
        } catch (Exception e) {
            logger.error("server start error",e);
        }finally{
            if(serverSocket!=null){
                logger.info("server is close");
                serverSocket.close();
                serverSocket = null;
            }
        }

    }

}
