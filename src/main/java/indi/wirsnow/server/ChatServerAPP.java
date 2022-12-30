package indi.wirsnow.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author : wirsnow
 * @date : 2022/12/8 19:24
 * @description : 服务端程序启动类
 */
public class ChatServerAPP {
    /*
    当有客户端连接时，服务器端会自动创建一个线程，用于处理该客户端的信息，
    服务器端可以同时处理多个客户端的信息，当客户端断开连接时，服务器端会自动关闭该客户端的线程。
    */
    // 线程池
    private static final ExecutorService threadPool = new ThreadPoolExecutor(
            2,
            5,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(56448)) {
            System.out.println("服务器已启动，等待客户端连接...");
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new ChatServerListen(socket));
                threadPool.execute(new ChatServerOutput(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
