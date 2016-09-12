package com.xiong.netty.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncServer>{

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncServer attachment) {
        attachment.getAsyncServerSocChannel().accept(attachment,this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncServer attachment) {
        exc.printStackTrace();
        attachment.getLatch().countDown();
    }

}