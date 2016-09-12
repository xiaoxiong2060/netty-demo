package com.xiong.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiplexerServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MultiplexerServer.class);

    private Selector            selector;

    private ServerSocketChannel serverChannel;

    private volatile boolean    stop;

    public MultiplexerServer(int port){
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("server is start in port " + port);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("server is error");
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKey = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKey.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (selector != null) {
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void handleInput(SelectionKey key) {

        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel sec = (ServerSocketChannel) key.channel();
                try {
                    SocketChannel sc = sec.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer bf = ByteBuffer.allocate(1024);
                try {
                    int readBytes = sc.read(bf);
                    if (readBytes > 0) {
                        bf.flip();
                        byte[] bytes = new byte[bf.remaining()];
                        bf.get(bytes);
                        String content = new String(bytes, "UTF-8");
                        logger.info(content);
                        String response = "fine thanks and you";
                        doWrite(sc, response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String response) {

        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            try {
                sc.write(writeBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
