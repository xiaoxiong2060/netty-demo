package com.xiong.netty.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncServer implements Runnable{
    
    private int port;
    
    private CountDownLatch latch;
    
    private AsynchronousServerSocketChannel asyncServerSocChannel;

    public AsyncServer(int port){
        this.setPort(port);
        try {
            asyncServerSocChannel = AsynchronousServerSocketChannel.open();
            asyncServerSocChannel.bind(new InetSocketAddress(port));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private void doAccept() {
        asyncServerSocChannel.accept(this, new AcceptCompletionHandler());
    }

    
    public AsynchronousServerSocketChannel getAsyncServerSocChannel() {
        return asyncServerSocChannel;
    }

    
    public void setAsyncServerSocChannel(AsynchronousServerSocketChannel asyncServerSocChannel) {
        this.asyncServerSocChannel = asyncServerSocChannel;
    }

    
    public CountDownLatch getLatch() {
        return latch;
    }

    
    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    

}
