package indi.wirsnow.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : wirsnow
 * @date : 2022/12/8 19:24
 * @description: 主要用于监听服务器端的端口
 */
public class ChatServerAPP {
    /*
    当有客户端连接时，服务器端会自动创建一个线程，用于处理该客户端的信息，
    服务器端可以同时处理多个客户端的信息，当客户端断开连接时，服务器端会自动关闭该客户端的线程。
    */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(56458)) {
            Socket socket;
            System.out.println("***服务器即将启动，等待客户端的连接***");
            while (true) {
                socket = serverSocket.accept();
                InetAddress inetAddress = socket.getInetAddress();
                ServerThread serverThread = new ServerThread(socket, inetAddress);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
