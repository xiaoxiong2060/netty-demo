package com.xiong.netty.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel asyncSocChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel asyncSocChannel){
        if (this.asyncSocChannel == null) {
            this.asyncSocChannel = asyncSocChannel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String req = new String(body, "UTF-8");
            System.out.println(req);
            doWrite("fine thanks and you?");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void doWrite(String req) {
        if (req != null && req.trim().length() > 0) {
            byte[] bytes = req.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            asyncSocChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (attachment.hasRemaining()) asyncSocChannel.write(attachment, attachment, this);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        asyncSocChannel.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.asyncSocChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
