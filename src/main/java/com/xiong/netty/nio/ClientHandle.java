package com.xiong.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandle implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandle.class);

    private String              host;
    private int                 port;
    private Selector            selector;
    private SocketChannel       socketChannel;
    private volatile boolean    stop;

    public ClientHandle(String string, int port){
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            } catch (IOException e) {
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
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                try {
                    if (sc.finishConnect()) {
                        sc.register(selector, SelectionKey.OP_READ);
                    }
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doWrite(sc);
            }
            if (key.isReadable()) {
                ByteBuffer bf = ByteBuffer.allocate(1024);
                try {
                    int readBytes = sc.read(bf);
                    if (readBytes > 0) {
                        bf.flip();
                        byte[] bytes = new byte[bf.remaining()];
                        bf.get(bytes);
                        String content = new String(bytes, "UTF-8");
                        logger.info(content);
                        this.stop = true;
                    } else if (readBytes < 0) {
                        key.cancel();
                        sc.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void doWrite(SocketChannel sc) {
        byte[] bytes = "How are you?".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        try {
            sc.write(writeBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doConnect() {
        try {
            if (socketChannel.connect(new InetSocketAddress(host, port))) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                doWrite(socketChannel);
            } else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
