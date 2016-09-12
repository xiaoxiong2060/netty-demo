package com.xiong.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
        String host = "127.0.0.1";
        BufferedReader input = null;
        PrintWriter output = null;
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output.println("HOW ARE YOU ?");
            logger.info("HOW ARE YOU ?");
            String request = input.readLine();
            logger.info(request);
        } catch (Exception e) {
            logger.error("client is error");
        } finally {
            if (input != null) {
                try {
                    input.close();
                    input = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                    output = null;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                socket = null;
            }
        }

    }

}
