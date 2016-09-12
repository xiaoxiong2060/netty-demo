package com.xiong.netty.pio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private Socket              socket;

    public ServerHandler(Socket socket){
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader input = null;
        PrintWriter output = null;
        try {
            input = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            output = new PrintWriter(getSocket().getOutputStream(), true);
            String body = null;
            while (true) {
                body = input.readLine();
                if (body == null) {
                    break;
                }
                if (body.equals("HOW ARE YOU ?")) {
                    output.println("FINE THANK YOU AND YOU?");
                } else {
                    logger.info("BAD QUERY");
                }
            }

        } catch (Exception e) {
            if(input!=null){
                try {
                    input.close();
                    input = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(output!=null){
                try {
                    output.close();
                    output = null;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            
            if(socket!=null){
                try {
                    socket.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                socket = null;
            }
        }
        

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
