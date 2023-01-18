package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.net.ServerSocket;

import java.util.concurrent.*;

import static indi.wirsnow.chatroom.util.ChatUtil.appendAndFlush;


/**
 * @author : wirsnow
 * @date : 2023/1/1 14:03
 * @description : 服务端线程类
 */
public class ServerThreadStart {
    private static final ExecutorService threadPool = new ThreadPoolExecutor(
            5,
            21,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("线程池已满，拒绝连接"));
    private final ChatUniversalData chatUniversalData;

    public ServerThreadStart(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
        connect();
    }

    public void connect() {
        JTextArea messageArea = chatUniversalData.getMessageArea();
        int port = Integer.parseInt(chatUniversalData.getPortField().getText());
        new Thread(() -> {
            // 创建服务器端ServerSocket，指定绑定的端口，并监听此端口
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("服务器启动成功");
                chatUniversalData.setConnected(true);
                appendAndFlush(messageArea, "服务器启动成功，等待客户端连接...\n");

                // 调用accept()方法开始监听，等待客户端的连接
                while (true) {
                    new Thread(new ServerMessageInput(serverSocket, chatUniversalData)).start();
                }
            } catch (Exception e) {
                chatUniversalData.setConnected(false);
                JOptionPane.showMessageDialog(null, "端口号已被占用", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }
}

