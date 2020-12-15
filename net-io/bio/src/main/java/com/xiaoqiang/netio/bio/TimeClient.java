package com.xiaoqiang.netio.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TimeClient {
    private final String host;
    private final int port;
    private final Socket socket;

    public TimeClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        socket = new Socket();
        connect();
    }

    private void connect() throws IOException {
        socket.connect(new InetSocketAddress(host, port));
    }

    public byte[] read() throws IOException {
        InputStream in = socket.getInputStream();

        int available = 0;
        while ((available=in.available())==0){
            Thread.yield();
        }
        byte[] data = new byte[available];
        in.read(data);
        return data;
    }

    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        TimeClient client = new TimeClient("localhost", 12345);
        System.out.println(new String(client.read()));
        client.close();
    }
}
