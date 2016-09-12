package com.xiong.netty.aio;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncClientHandler implements CompletionHandler<Void, AsyncClientHandler>,Runnable{
    
    private AsynchronousSocketChannel client;
    private String host;
    private int port;
    private CountDownLatch latch;

    
    public AsyncClientHandler(String host, int port){
        super();
        this.host = host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncClientHandler attachment) {
        
    }

    @Override
    public void failed(Throwable exc, AsyncClientHandler attachment) {
        
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }

}
