package com.xiaoqiang.netio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TimeServer {
    public static final int DEFAULT_PORT = 12345;
    public static final String TIME_TEMPLATE = "yyyy-MM-dd HH:mm:ss";


    private final ServerSocket serverSocket;

    private final ServerSocketChannel channel;
    private final Selector selector;

    public TimeServer() throws IOException {
        this(DEFAULT_PORT);
    }

    public TimeServer(int port) throws IOException {
        channel = ServerSocketChannel.open();
        serverSocket = channel.socket();
        serverSocket.bind(new InetSocketAddress(port));
        selector = Selector.open();
    }

    public void start() throws IOException {
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        for (; ; ) {
            selector.select();
            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keySet.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey currentKey = keyIterator.next();
                keyIterator.remove();
                keyHandler(currentKey);
            }
        }
    }

    public void keyHandler(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                acceptHandler(key);
            }
            if (key.isReadable()) {
                readHandler(key);
            }
            if (key.isWritable()) {
                writableHandler(key);
            }
            if (key.isConnectable()) {
                connectHandler(key);
            }
        } catch (Exception e){

        }
    }

    private void connectHandler(SelectionKey key) throws IOException {
        //...
    }

    private void writableHandler(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        SocketAddress address = clientChannel.getRemoteAddress();
        String time = new SimpleDateFormat(TIME_TEMPLATE).format(new Date());
        ByteBuffer buffer = ByteBuffer.wrap(time.getBytes(StandardCharsets.UTF_8));
        while (buffer.hasRemaining()) {
            if (clientChannel.write(buffer)==0) {
                break;
            }
        }
        System.out.printf("%10s%10s 写入: %10s。\n", time, address, time);
        clientChannel.close();
    }

    private void readHandler(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = channel.accept();
        SocketAddress address = clientChannel.getRemoteAddress();
        //read op
        System.out.printf("%10s%10s 收到: %s。\n", new SimpleDateFormat(TIME_TEMPLATE).format(new Date()), address, new SimpleDateFormat(TIME_TEMPLATE).format(new Date()));
        clientChannel.close();
    }

    public void acceptHandler(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = channel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_CONNECT|SelectionKey.OP_WRITE);
    }

    public void close() throws IOException {
        channel.close();
        selector.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TimeServer server = new TimeServer();
        server.start();
        TimeUnit.MINUTES.sleep(10);
        server.close();
    }
}
