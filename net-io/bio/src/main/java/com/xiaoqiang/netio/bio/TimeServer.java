package com.xiaoqiang.netio.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimeServer {
    public static final int DEFAULT_THREAD = 10;
    public static final int DEFAULT_PORT = 12345;
    public static final String TIME_TEMPLATE = "yyyy-MM-dd HH:mm:ss";


    private final ServerSocket serverSocket;

    private final ExecutorService service;

    public TimeServer() throws IOException {
        this(DEFAULT_PORT, DEFAULT_THREAD);
    }

    public TimeServer(int port, int threadNums) throws IOException {
        service = Executors.newFixedThreadPool(threadNums);
        serverSocket = new ServerSocket();
        SocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);
    }

    public void start() throws IOException {
        Socket client = null;
        while ((client = serverSocket.accept()) != null) {
            Socket finalClient = client;
            service.submit(()-> handler(finalClient));
        }
    }

    public void handler(Socket client){
        String time = String.format("Now: %s", new SimpleDateFormat(TIME_TEMPLATE).format(new Date()));
        try {
            OutputStream out = client.getOutputStream();
            out.write(time.getBytes());
            out.flush();
        } catch (IOException e) {
            System.out.println("Client error:"+e.getMessage());
        }
    }

    public void close() throws IOException {
        serverSocket.close();
        service.shutdown();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TimeServer server = new TimeServer();
        server.start();
        TimeUnit.MINUTES.sleep(10);
        server.close();
    }
}
