package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.swingui.ChatFrame;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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

    private static final JTextArea messageArea = new JTextArea();
    private static final JTextArea editorArea = new JTextArea();
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    // 线程池
    private static final ExecutorService threadPool = new ThreadPoolExecutor(
            2,
            50,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("线程池已满，拒绝连接"));

    public static void main(String[] args) throws IOException {
        Map<String, Socket> map = new HashMap<>();
        new ChatFrame(null, messageArea, editorArea, map);
        try (ServerSocket serverSocket = new ServerSocket(56448)) {

            //存取用户信息（用户名和Socket）


            System.out.println("服务器已启动，等待客户端连接...");

            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new ChatServerThread(socket, map, messageArea));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
