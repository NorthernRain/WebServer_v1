package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LeafDust
 * @create 2019-08-23 9:54
 */
public class Server {
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public Server() {
        try {
            System.out.println("服务器启动中：");
            serverSocket = new ServerSocket(9600);
            executorService = Executors.newFixedThreadPool(50);
            System.out.println("服务器启动完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try {
            Socket socket = null;
            while (true) {
                System.out.println("等待连接...");
                socket = serverSocket.accept();
                System.out.println("一个客户端已连接！");
                ClientHandler clientHandler = new ClientHandler(socket);
                executorService.execute(clientHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
